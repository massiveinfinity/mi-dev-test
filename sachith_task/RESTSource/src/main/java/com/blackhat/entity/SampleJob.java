package com.blackhat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "getAllSampleJobs", query = "from SampleJob s"),
    @NamedQuery(name = "getSampleJobById", query = "from SampleJob s where s.id = :id")})
@Entity
@Table(name = "SampleJob", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID"}),
    @UniqueConstraint(columnNames = {"REFERENCE_NO"})})
public class SampleJob implements Serializable {

    private static final long serialVersionUID = 7907650640081075587L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private JobOrder orderId;

    @NotNull
    @Column(name = "TITLE", length = 100)
    private String title;

    @NotNull
    @Column(name = "REFERENCE_NO", length = 75)
    private String referenceNumber;

    @Column(name = "PRIORITY")
    private int priority;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "ACCEPTED_DATE")
    private Date acceptedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @NotNull
    @Column(name = "STATUS")
    private char status;

    @NotNull
    @Column(name = "CREATE_USER", length = 30)
    private String createUser;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME")
    private Date createDate;

    @NotNull
    @Column(name = "LASTUPDATE_USER", length = 30)
    private String lastUpdateUser;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "LASTUPDATE_DATETIME")
    private Date lastUpdateDateTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "jobId")
    private List<Screen> screens;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "jobId")
    private List<Artwork> artworks;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "jobId")
    private List<DeliveryAddress> deliveryAddresses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "jobId")
    private List<Part> parts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "jobId")
    private List<Sample> samples;

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
     * @return the orderId
     */
    public JobOrder getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(JobOrder orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return the acceptedDate
     */
    public Date getAcceptedDate() {
        return acceptedDate;
    }

    /**
     * @param acceptedDate the acceptedDate to set
     */
    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the status
     */
    public char getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(char status) {
        this.status = status;
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
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    /**
     * @return the screens
     */
    public List<Screen> getScreens() {
        return screens;
    }

    /**
     * @param screens the screens to set
     */
    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    /**
     * @return the artworks
     */
    public List<Artwork> getArtworks() {
        return artworks;
    }

    /**
     * @param artworks the artworks to set
     */
    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }

    /**
     * @return the deliveryAddresses
     */
    public List<DeliveryAddress> getDeliveryAddresses() {
        return deliveryAddresses;
    }

    /**
     * @param deliveryAddresses the deliveryAddresses to set
     */
    public void setDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }

    /**
     * @return the parts
     */
    public List<Part> getParts() {
        return parts;
    }

    /**
     * @param parts the parts to set
     */
    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
