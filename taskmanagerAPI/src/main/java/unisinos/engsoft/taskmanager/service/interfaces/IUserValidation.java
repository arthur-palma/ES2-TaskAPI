package unisinos.engsoft.taskmanager.service.interfaces;


import unisinos.engsoft.taskmanager.model.Users;

public interface IUserValidation {

    Users validateUserById(int id);

    Users validateUserByEmail(String email);

}
