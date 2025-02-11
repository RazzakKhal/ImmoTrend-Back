package immotrend.com.immotrend.services;

import immotrend.com.immotrend.dtos.fromView.LoginUserDto;
import immotrend.com.immotrend.dtos.toView.UserDto;
import immotrend.com.immotrend.entities.User;
import immotrend.com.immotrend.mappers.UserMapper;
import immotrend.com.immotrend.repositories.UserRepository;
import immotrend.com.immotrend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageService imageService;

    @Value("${api.url}")
    private String APPLICATION_URL;

    /**
     * permet la creation d'un utulisateur si il n'existe pas déjà en BDD, et la connexion en retournant le token
     * @return
     */
    @Override
    public Map<String, String> createUser(String name,
                                          String firstname,
                                          String email,
                                          String password,
                                          MultipartFile photo){
        if(userRepository.findByEmail(email).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"un utilisateur avec cette identifiant existe déjà");
        }
        User user= new User();
        user.setName(name);
        user.setFirstname(firstname);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        // gérer le traitement de la carte vitale
        try{
            String imageUrl = imageService.saveFileOnServerAndReturnFileUrl(photo);
            user.setPhoto(APPLICATION_URL + imageUrl);
        }catch(IOException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "oops problème dans le traitement de l'image");
        }

        userRepository.save(user);
        String token = jwtUtil.generateToken(user);
        return Map.of("token", token);
    }

    /**
     * permet la creation d'un utulisateur si il n'existe pas déjà en BDD, et la connexion en retournant le token
     * @return
     */
    @Override
    public Map<String, String> createUser(String name,
                                          String firstname,
                                          String email,
                                          String password){
        if(userRepository.findByEmail(email).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"un utilisateur avec cette identifiant existe déjà");
        }
        User user= new User();
        user.setName(name);
        user.setFirstname(firstname);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));


        userRepository.save(user);
        String token = jwtUtil.generateToken(user);
        return Map.of("token", token);
    }

    /**
     * permet la connexion de l'utilisateur en lui retournant un token si la combinaison id/mdp est correct
     * @param loginUserDto
     * @return
     */
    @Override
    public Map<String, String> loginUser(LoginUserDto loginUserDto){
        User user = userMapper.toEntity(loginUserDto);
        Optional<User> userBdd = userRepository.findByEmail(user.getEmail());
        if(userBdd.isEmpty() || !bCryptPasswordEncoder.matches(user.getPassword(),userBdd.get().getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"la combinaison nom d'utilisateur / mot de passe ne correspond à aucun utlisateur");
        }
        String token = jwtUtil.generateToken(userBdd.get());
        return Map.of("token", token);
    }

    /**
     * retourne l'utilisateur actuellement connecté
     * @param principal
     * @return
     */
    @Override
    public UserDto getMe(Object principal){
        return userMapper.toDto((User) principal);
    }
}