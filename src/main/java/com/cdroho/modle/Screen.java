package com.cdroho.modle;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 屏幕管理
 * @author HZL
 * @date 2020-5-05
 */
@Entity
@Table(name = "screen")
public class Screen implements Serializable {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private long id;

    private String ip;

    private String name;

    private String profile;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", foreignKey = @ForeignKey(name = "FK_Reference_2313"))
    private NurseTriage nurseTriage;


}