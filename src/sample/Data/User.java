package sample.Data;

import java.util.Date;

public class User {
    private String id ="";
    private String login="";
    private String pass="";
    private UserType group=UserType.User;
    private byte[] salt={};
    private Boolean isBlock =false;
    private Date dateBlock = new Date();
    public enum UserType {
        Admin,
        User
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setGroup(String group) {
        this.group = UserType.valueOf(group);
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void setIsBlock(Boolean isBlock) {
        this.isBlock = isBlock;
    }

    public void setDateBlock(Date dateBlock) {
        this.dateBlock = dateBlock;
    }

    public User(){

    }
    public User(String login, String pass, UserType group, byte[] salt){
        this.login = login;
        this.pass = pass;
        this.group = group;
        this.salt = salt;
    }
    public User(String id, String login, String pass, UserType group,Boolean isBlock){
        this.login = login;
        this.pass = pass;
        this.group = group;
        this.id = id;
        this.isBlock = isBlock;
    }

    public Boolean getIsBlock() {
        return isBlock;
    }

    public String getGroup() {
        return group.name();
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getId() {
        return id;
    }

    public Date getDateBlock() {
        return dateBlock;
    }
}
