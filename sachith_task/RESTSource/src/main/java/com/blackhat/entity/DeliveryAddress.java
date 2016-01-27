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

/**
 *
 * @author Sachith Dickwella
 */
@NamedQueries({
    @NamedQuery(name = "getAllDeliveryAddresses", query = "from DeliveryAddress d"),
    @NamedQuery(name = "getDeliveryAddressById", query = "from DeliveryAddress d where d.id = :id")})
@Entity
@Table(name = "DeliveryAddress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID"})})
public class DeliveryAddress implements Serializable {

    private static final long serialVersionUID = -9092229007232139499L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "JOB_ID", referencedColumnName = "ID")
    private SampleJob jobId;

    @NotNull
    @Column(name = "ADDRESS_LINE1", length = 40)
    private String addressLine1;

    @Column(name = "ADDRESS_LINE2", length = 40)
    private String addressLine2;

    @NotNull
    @Column(name = "CITY_TOWN", length = 30)
    private String cityTown;

    @Column(name = "STATE_PROVINCE", length = 50)
    private String stateProvince;

    @Column(name = "ZIP_CODE", length = 10)
    private String zipCode;

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
     * @return the addressLine1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * @param addressLine1 the addressLine1 to set
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * @return the addressLine2
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * @param addressLine2 the addressLine2 to set
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * @return the cityTown
     */
    public String getCityTown() {
        return cityTown;
    }

    /**
     * @param cityTown the cityTown to set
     */
    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    /**
     * @return the stateProvince
     */
    public String getStateProvince() {
        return stateProvince;
    }

    /**
     * @param stateProvince the stateProvince to set
     */
    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
