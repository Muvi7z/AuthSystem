package sample.Data;

public class User {
    private String id ="";
    private String login="";
    private String pass="";
    private String group="";
    private byte[] salt={};
    private Boolean is_block =false;
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
        this.group = group;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void setIs_block(Boolean is_block) {
        this.is_block = is_block;
    }

    public User(){

    }
    public User(String login, String pass, UserType group, byte[] salt){
        this.login = login;
        this.pass = pass;
        this.group = group.name();
        this.salt = salt;
    }
    public User(String id, String login, String pass, UserType group,Boolean is_block){
        this.login = login;
        this.pass = pass;
        this.group = group.name();
        this.id = id;
        this.is_block = is_block;
    }

    public Boolean getIs_block() {
        return is_block;
    }

    public String getGroup() {
        return group;
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
}
