
package com.example.toni.flowerfarm.Admin.Model;

/**
 * Created by toni on 3/26/17.
 */

public class ExpertsModel {

    private String name;
    private String email;
    private String password;

    public ExpertsModel() {
    }

    public ExpertsModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
