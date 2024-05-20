package org.silver.seeders;

import org.silver.models.entities.UserEntity;
import org.silver.repositories.IUserRepository;
import org.springframework.stereotype.Component;

/**
 * Cargar Users al iniciar el proyecto
 * */
@Component
public class UserSeeder {

    private final IUserRepository userRepository;

    public UserSeeder(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void loadData() {
        if (userRepository.count() == 0){
            UserEntity user1 = new UserEntity();
            UserEntity user2 = new UserEntity();
            UserEntity user3 = new UserEntity();

            user1.setUsername("juan_perez");
            user2.setUsername("pedro_lopez");
            user3.setUsername("mario1234");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

        }
    }

}
