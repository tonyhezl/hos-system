package com.cdroho.modle;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


/**
 * 角色
 * 用户和角色，角色与权限之间都是ManyToMany的对应关系
 * @author HZL
 * @date 2020-4-7
 */
@Entity
@Table(name = "role_t")
public class SysRole implements Serializable {
    private static final long serialVersionUID = -8687790154329829056L;
    @Id
    @GeneratedValue
    private Integer id;
    private String role;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission_t", joinColumns = { @JoinColumn(name = "rid") }, inverseJoinColumns = {
            @JoinColumn(name = "pid") })
    private Set<SysPermission> permissions;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_t", joinColumns = { @JoinColumn(name = "rid") }, inverseJoinColumns = {
            @JoinColumn(name = "uid") })
    private Set<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}

