package com.blackhat.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "getAllParts", query = "from Part p"),
    @NamedQuery(name = "getPartById", query = "from Part p where p.id = :id")})
@Entity
@Table(name = "Part", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID"})})
public class Part implements Serializable {

    private static final long serialVersionUID = -7854681959193849256L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "JOB_ID", referencedColumnName = "ID")
    private SampleJob jobId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PANTONECOLOR_ID", referencedColumnName = "ID")
    private PantoneColor pantoneColorId;

    @NotNull
    @Column(name = "PART_NAME", length = 100)
    private String partName;

    @Type(type = "text")
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "CREATE_USER", length = 30)
    private String createUser;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME")
    private Date createDateTime;

    @NotNull
    @Column(name = "LASTUPDATE_USER", length = 30)
    private String lastUpdateUser;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "LASTUPDATE_DATETIME")
    private Date lastUpdateDateTime;

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
     * @return the jobId
     */
    public SampleJob getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(SampleJob jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the pantoneColorId
     */
    public PantoneColor getPantoneColorId() {
        return pantoneColorId;
    }

    /**
     * @param pantoneColorId the pantoneColorId to set
     */
    public void setPantoneColorId(PantoneColor pantoneColorId) {
        this.pantoneColorId = pantoneColorId;
    }

    /**
     * @return the partName
     */
    public String getPartName() {
        return partName;
    }

    /**
     * @param partName the partName to set
     */
    public void setPartName(String partName) {
        this.partName = partName;
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
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the createDateTime
     */
    public Date getCreateDateTime() {
        return createDateTime;
    }

    /**
     * @param createDateTime the createDateTime to set
     */
    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /**
     * @return the lastUpdateUser
     */
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    /**
     * @param lastUpdateUser the lastUpdateUser to set
     */
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    /**
     * @return the lastUpdateDateTime
     */
    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    /**
     * @param lastUpdateDateTime the lastUpdateDateTime to set
     */
    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }
}
