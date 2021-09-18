package sample;

public class User {

    private String login="";
    private String pass="";
    private String group="";
    private Boolean is_block =false;
    public static enum UserType {
        Admin,
        User
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public User(String login, String pass, UserType group ){
        this.login = login;
        this.pass = pass;
        this.group = group.name();
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
}
