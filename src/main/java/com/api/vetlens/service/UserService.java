package com.api.vetlens.service;

import com.api.vetlens.dto.*;
import com.api.vetlens.dto.dog.DogRequestDTO;
import com.api.vetlens.dto.dog.DogResponseDTO;
import com.api.vetlens.dto.user.UserResponseDTO;
import com.api.vetlens.dto.user.UserUpdateRequestDTO;
import com.api.vetlens.entity.Dog;
import com.api.vetlens.entity.Role;
import com.api.vetlens.entity.Sex;
import com.api.vetlens.entity.User;
import com.api.vetlens.exceptions.ApiException;
import com.api.vetlens.exceptions.NotFoundException;
import com.api.vetlens.repository.DogRepository;
import com.api.vetlens.repository.UserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final Faker faker;
    private final EmailService emailService;
    private ModelMapper mapper = new ModelMapper();

    public UserResponseDTO update(UserUpdateRequestDTO request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            User savedUser = userRepository.save(user);
            return mapper.map(savedUser, UserResponseDTO.class);
        }
        throw new NotFoundException("Usuario " + request.getUsername() + " no encontrado");
    }

    public boolean checkUsernameAvailability(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            return false;
        }
        return true;
    }

    public MessageDTO changePassword(String username, String oldPassword, String newPassword) {
        User user = getUser(username);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new NotFoundException("Contraseña incorrecta");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageDTO("Contraseña actualizada exitosamente");
    }

    public MessageDTO forgotPassword(String username) {

        User user = getUser(username);
        String pass = faker.internet().password();
        user.setPassword(passwordEncoder.encode(pass));

        this.sendEmailChangedPass(user.getEmail(), pass);
        userRepository.save(user);

        return new MessageDTO("Se ha enviado un mail con una nueva contraseña");
    }

    public DogResponseDTO addDog(DogRequestDTO request) {
        log.info("Buscando el dueño");
        User user = getUser(request.getOwnerUsername());
        Dog dog = new Dog();
        dog.setDogBreed(request.getDogBreed());
        dog.setName(request.getName());
        dog.setOwner(user);
        dog.setDateOfBirth(request.getDateOfBirth());
        dog.setCastrated(request.isCastrated());
        dog.setSex(Sex.valueOf(request.getSex()));
        dog.setPhotoUrl("https://vetlens.s3.sa-east-1.amazonaws.com/vetlens.png");
        dog.setDeleted(false);
        log.info("Seteando información del perro y guardandolo en la base de datos");
        return mapper.map(dogRepository.save(dog), DogResponseDTO.class);
    }

    public MessageDTO uploadFile (MultipartFile multipartFile, String username) {
        s3Service.upload("user-" + userRepository.findByUsername(username).get().getId() + "-file ID: (" + UUID.randomUUID() + ")", multipartFile);
        return new MessageDTO("Se subió el archivo correctamente");
    }

    public DogResponseDTO updateDog(DogRequestDTO request) {
        getUser(request.getOwnerUsername());
        Optional<Dog> dogOptional = dogRepository.findByName(request.getName());
        if (dogOptional.isEmpty()) {
            throw new NotFoundException("Perro " + request.getName() + " no encontrado");
        }
        Dog dog = dogOptional.get();
        dog.setDogBreed(request.getDogBreed());
        dog.setDateOfBirth(request.getDateOfBirth());
        dog.setCastrated(request.isCastrated());
        dog.setSex(Sex.valueOf(request.getSex()));
        return mapper.map(dogRepository.save(dog), DogResponseDTO.class);
    }

    public MessageDTO addDogPhoto(Integer dogId, MultipartFile photo){
        Optional<Dog> dogOptional = dogRepository.findById(dogId);
        if(dogOptional.isEmpty()){
            throw new NotFoundException("El perro no existe");
        }
        Dog dog = dogOptional.get();
        String photoUrl = s3Service.upload("profile-" + dog.getName(), photo);
        dog.setPhotoUrl(photoUrl);
        dogRepository.save(dog);
        return new MessageDTO(photoUrl);
    }

    public MessageDTO removeDogPhoto(Integer dogId){
        Optional<Dog> dogOptional = dogRepository.findById(dogId);
        if(dogOptional.isEmpty()){
            throw new NotFoundException("El perro no existe");
        }
        Dog dog = dogOptional.get();
        s3Service.removeDogPhoto(dog.getName());
        dog.setPhotoUrl(null);
        dogRepository.save(dog);
        return new MessageDTO("Foto eliminada");
    }

    public MessageDTO removeDog(Integer dogId){
        Optional<Dog> dogOptional = dogRepository.findById(dogId);
        if(dogOptional.isEmpty()){
            throw new NotFoundException("El perro no existe");
        }
        Dog dog = dogOptional.get();
        s3Service.removeDogPhoto(dog.getName());
        dog.setDeleted(true);
        dogRepository.save(dog);
        return new MessageDTO("Perro eliminado");
    }

    public List<DogResponseDTO> getAllDogs(String username) {
        List<Dog> dogs = dogRepository.findAllByOwner(getUser(username));
        return dogs.stream().map(
                dog -> mapper.map(dog, DogResponseDTO.class)
        ).collect(Collectors.toList());
    }

    public List<Dog> getDogsList(String username) {
        return dogRepository.findAllByOwner(getUser(username));
    }

    public User getUserById(Integer id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()){
            throw new NotFoundException("Usuario no encontrado");
        }
        return userOptional.get();
    }

    public User getUser(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()){
            log.error("El usuario no fue encontrado");
            throw new NotFoundException("Usuario " + username + " no encontrado");
        }
        return userOptional.get();
    }

    public UserResponseDTO getUserByUsername(String username) {
        return mapper.map(getUser(username), UserResponseDTO.class);
    }

    public void sendEmailNewAccount(String email, String password, String user, Role role) {
        if (role == Role.VET) {
            emailService.sendEmail(email, "CUENTA CREADA CON EXITO", "<html>\n" +
                    "\n" +
                    "<body>\n" +
                    "    <div>\n" +
                    "        <h1 style=\"color: #00A6B0\">¡Bienvenido a VetLens!</h1>\n" +
                    "        <h2>Su cuenta ha sido creada con exito</h2> \n" +
                    "        <p> \n" +
                    "            Le informamos que su cuenta fue creada exitosamente, sus credenciales son: \n" +
                    "        </p> \n" +
                    "        <ul> \n" +
                    "            <li> \n" +
                    "                <p><u>Usuario</u>: " + user + "</p> \n" +
                    "                </li> \n" +
                    "            <li> \n" +
                    "                <p><u>Contraseña</u>:  " + password +  "</p> \n" +
                    "                </li>  \n" +
                    "            </ul> \n" +
                    "        </p> \n" +
                    "         <br>  \n" +
                    "        <p> \n" +
                    "            Sin embargo, aún debemos verificar que su número de matricula sea válida. Es por ello que aún no tendrá acceso a la aplicación." +
                    " Nos comunicaremos con usted una vez que la verificación haya sido realizada. \n" +
                    "            </p>\n" +
                    "        <p><b>Atentamente,</b></p> \n" +
                    "\n" +
                    "        <p><b>Equipo de VetLens</b></p> \n" +
                    "         </div> \n" +
                    "    </body> \n" +
                    "\n" +
                    "\n" +
                    "</html>");
        } else if (role == Role.STUDENT) {
            emailService.sendEmail(email, "CUENTA CREADA CON EXITO", "<html>\n" +
                    "\n" +
                    "<body>\n" +
                    "    <div>\n" +
                    "        <h1 style=\"color: #00A6B0\">¡Bienvenido a VetLens!</h1>\n" +
                    "        <h2>Su cuenta ha sido creada con exito</h2> \n" +
                    "        <p> \n" +
                    "            Le informamos que su cuenta fue creada exitosamente, sus credenciales son: \n" +
                    "        </p> \n" +
                    "        <ul> \n" +
                    "            <li> \n" +
                    "                <p><u>Usuario</u>: " + user + "</p> \n" +
                    "                </li> \n" +
                    "            <li> \n" +
                    "                <p><u>Contraseña</u>:  " + password +  "</p> \n" +
                    "                </li>  \n" +
                    "            </ul> \n" +
                    "        </p> \n" +
                    "         <br>  \n" +
                    "        <p> \n" +
                    "            Sin embargo, aún debemos verificar su certificado de alumno regular sea válido. Es por ello que aún no tendrá acceso a la aplicación." +
                    " Nos comunicaremos con usted una vez que la verificación haya sido realizada. \n" +
                    "            </p>\n" +
                    "        <p><b>Atentamente,</b></p> \n" +
                    "\n" +
                    "        <p><b>Equipo de VetLens</b></p> \n" +
                    "         </div> \n" +
                    "    </body> \n" +
                    "\n" +
                    "\n" +
                    "</html>");
        }
        else {
            emailService.sendEmail(email, "CUENTA CREADA CON EXITO", "" +
                    "<html>" +
                    "<body>" +
                    "   <div >" +
                    "   <h1 style='color: #00A6B0'>¡Bienvenido a VetLens!</h1>" +
                    "   <h2>Su cuenta ha sido creada con exito</h2>" +
                    "<p>" +
                    "Le informamos que su cuenta fue creada exitosamente, sus credenciales son:" +
                    "</p>" +
                    "<ul>" +
                    "<li>" +
                    "<p><u>Usuario</u>: " + user + "</p>" +
                    "</li>" +
                    "<li>" +
                    "<p><u>Contraseña</u>: " + password + "</p>" +
                    "</li> " +
                    "</ul>" +
                    "</p>" +
                    "   <br> " +
                    "<p>" +
                    "Ya puede iniciar sesión en la aplicación y utilizar VetLens. Gracias por registrarte!" +
                    "</p>" +
                    "<p><b> Atentamente, </b></p>" +
                    "<p><b> Equipo de VetLens</b></p>" +
                    "   </div>" +
                    "</body>" +
                    "</html>");
        }
    }

    private void sendEmailChangedPass(String email, String password) {
        emailService.sendEmail(email, "CONTRASEÑA MODIFICADA CON EXITO", "" +
                " <html>" +
                "<body>" +
                "<div style='font-family:sans-serif;'>" +
                "<h1>VetLens</h1>" +
                "<h2>Su contraseña ha sido modificada</h2>" +
                "<h3>" +
                "Le informamos que la contraseña de su cuenta fue actualizada exitosamente, su nueva contraseña es:" +
                "</h3>" +
                "<ul>" +
                "<li>" +
                "<h3><u>Contraseña</u>: " + password + "</h3>" +
                "</li>" +
                "</ul>" +
                "<p>" +
                "Ya puede loguearse y disfrutar de la aplicación nuevamente." +
                "</p>" +
                "<p><b>-VetLens Team</b></p>" +
                "</div>" +
                "</body>" +
                "</html>");
    }

    public void sendEmailWithQR(String username, MultipartFile qr){
        User user = getUser(username);
        try{
            File convFile = new File(Objects.requireNonNull(qr.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream( convFile );
            fos.write(qr.getBytes());
            fos.close();
            emailService.sendEmailWithAttachment(user.getEmail(),
                    "NUEVO DIAGNÓSTICO",
                    "" +
                            " <html>" +
                            "<body>" +
                            "<div style='font-family:sans-serif;'>" +
                            "<h1>VetLens</h1>" +
                            "<h2>Se ha creado un nuevo diagnóstico</h2>" +
                            "<h3>" +
                            "Le adjuntamos el código QR para consultar información de su diagnóstico" +
                            "</h3>" +
                            "<p>" +
                            "Gracias por usar VetLens." +
                            "</p>" +
                            "<p><b>-VetLens Team</b></p>" +
                            "</div>" +
                            "</body>" +
                            "</html>",
                    convFile);
        } catch (IOException exception) {
            throw new ApiException("Error enviando el QR por mail");
        }

    }
}
