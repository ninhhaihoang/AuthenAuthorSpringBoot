package com.example.demo.Model;

import javax.persistence.*;

@Entity
@Table(name = "ROLES",
        uniqueConstraints = {
        @UniqueConstraint(name = "Role_UK", columnNames = "ROLE_NAME")})
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private int roleId;

    @Column(name = "ROLE_NAME", nullable = false, length = 45)
    private String roleName;

    public RoleEntity(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public RoleEntity() {
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
