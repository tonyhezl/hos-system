package com.cdroho.modle;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

/**
 * 权限
 * 用户和角色，角色与权限之间都是ManyToMany的对应关系
 * @author HZL
 * @date 2020-4-7
 */
@Entity
@Table(name = "permission_t")
public class SysPermission implements Serializable {
    private static final long serialVersionUID = 353629772108330570L;
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission_t", joinColumns = { @JoinColumn(name = "pid") }, inverseJoinColumns = {
            @JoinColumn(name = "rid") })
    private Set<SysRole> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }
}

