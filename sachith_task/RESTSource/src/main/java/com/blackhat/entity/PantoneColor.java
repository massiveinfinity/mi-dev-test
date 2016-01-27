package com.blackhat.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

/**
 *
 * @author Sachith Dickwella
 */
@NamedQueries({
    @NamedQuery(name = "getAllPantoneColors", query = "from PantoneColor p"),
    @NamedQuery(name = "getPantoneColorById", query = "from PantoneColor p where p.id = :id")})
@Entity
@Table(name = "PantoneColor", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID"})})
public class PantoneColor implements Serializable {

    private static final long serialVersionUID = -9051039421864097178L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @NotNull
    @Column(name = "NAME", length = 100)
    private String name;

    @NotNull
    @Column(name = "CODE", length = 45)
    private String code;

    @Type(type = "text")
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "HEX_CODE", length = 7)
    private String hexCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pantoneColorId")
    private List<Part> parts;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the hexCode
     */
    public String getHexCode() {
        return hexCode;
    }

    /**
     * @param hexCode the hexCode to set
     */
    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    /**
     * @return the parts
     */
    public List<Part> getPart() {
        return parts;
    }

    /**
     * @param parts the parts to set
     */
    public void setPart(List<Part> parts) {
        this.parts = parts;
    }
}
