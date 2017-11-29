package com.muelpatmore.googlemapsdemo.apimodels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("Badges")
    @Expose
    private List<Object> badges = null;
    @SerializedName("Score")
    @Expose
    private Double score;
    @SerializedName("DriveDistance")
    @Expose
    private Double driveDistance;
    @SerializedName("DriveInfoCalculated")
    @Expose
    private Boolean driveInfoCalculated;
    @SerializedName("NewnessDate")
    @Expose
    private String newnessDate;
    @SerializedName("DeliveryMenuId")
    @Expose
    private Integer deliveryMenuId;
    @SerializedName("DeliveryOpeningTime")
    @Expose
    private String deliveryOpeningTime;
    @SerializedName("DeliveryCost")
    @Expose
    private Integer deliveryCost;
    @SerializedName("MinimumDeliveryValue")
    @Expose
    private Double minimumDeliveryValue;
    @SerializedName("DeliveryTimeMinutes")
    @Expose
    private Object deliveryTimeMinutes;
    @SerializedName("DeliveryWorkingTimeMinutes")
    @Expose
    private Integer deliveryWorkingTimeMinutes;
    @SerializedName("OpeningTime")
    @Expose
    private String openingTime;
    @SerializedName("OpeningTimeIso")
    @Expose
    private String openingTimeIso;
    @SerializedName("SendsOnItsWayNotifications")
    @Expose
    private Boolean sendsOnItsWayNotifications;
    @SerializedName("RatingAverage")
    @Expose
    private Double ratingAverage;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("Tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Postcode")
    @Expose
    private String postcode;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("CuisineTypes")
    @Expose
    private List<CuisineType> cuisineTypes = null;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("IsOpenNow")
    @Expose
    private Boolean isOpenNow;
    @SerializedName("IsPremier")
    @Expose
    private Boolean isPremier;
    @SerializedName("IsSponsored")
    @Expose
    private Boolean isSponsored;
    @SerializedName("SponsoredPosition")
    @Expose
    private Integer sponsoredPosition;
    @SerializedName("IsNew")
    @Expose
    private Boolean isNew;
    @SerializedName("IsTemporarilyOffline")
    @Expose
    private Boolean isTemporarilyOffline;
    @SerializedName("ReasonWhyTemporarilyOffline")
    @Expose
    private String reasonWhyTemporarilyOffline;
    @SerializedName("UniqueName")
    @Expose
    private String uniqueName;
    @SerializedName("IsCloseBy")
    @Expose
    private Boolean isCloseBy;
    @SerializedName("IsHalal")
    @Expose
    private Boolean isHalal;
    @SerializedName("IsTestRestaurant")
    @Expose
    private Boolean isTestRestaurant;
    @SerializedName("DefaultDisplayRank")
    @Expose
    private Integer defaultDisplayRank;
    @SerializedName("IsOpenNowForDelivery")
    @Expose
    private Boolean isOpenNowForDelivery;
    @SerializedName("IsOpenNowForCollection")
    @Expose
    private Boolean isOpenNowForCollection;
    @SerializedName("RatingStars")
    @Expose
    private Integer ratingStars;
    @SerializedName("Logo")
    @Expose
    private List<Logo> logo = null;
    @SerializedName("Deals")
    @Expose
    private List<Object> deals = null;
    @SerializedName("NumberOfRatings")
    @Expose
    private Integer numberOfRatings;

    public List<Object> getBadges() {
        return badges;
    }

    public void setBadges(List<Object> badges) {
        this.badges = badges;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getDriveDistance() {
        return driveDistance;
    }

    public void setDriveDistance(Double driveDistance) {
        this.driveDistance = driveDistance;
    }

    public Boolean getDriveInfoCalculated() {
        return driveInfoCalculated;
    }

    public void setDriveInfoCalculated(Boolean driveInfoCalculated) {
        this.driveInfoCalculated = driveInfoCalculated;
    }

    public String getNewnessDate() {
        return newnessDate;
    }

    public void setNewnessDate(String newnessDate) {
        this.newnessDate = newnessDate;
    }

    public Integer getDeliveryMenuId() {
        return deliveryMenuId;
    }

    public void setDeliveryMenuId(Integer deliveryMenuId) {
        this.deliveryMenuId = deliveryMenuId;
    }

    public String getDeliveryOpeningTime() {
        return deliveryOpeningTime;
    }

    public void setDeliveryOpeningTime(String deliveryOpeningTime) {
        this.deliveryOpeningTime = deliveryOpeningTime;
    }

    public Integer getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Integer deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Double getMinimumDeliveryValue() {
        return minimumDeliveryValue;
    }

    public void setMinimumDeliveryValue(Double minimumDeliveryValue) {
        this.minimumDeliveryValue = minimumDeliveryValue;
    }

    public Object getDeliveryTimeMinutes() {
        return deliveryTimeMinutes;
    }

    public void setDeliveryTimeMinutes(Object deliveryTimeMinutes) {
        this.deliveryTimeMinutes = deliveryTimeMinutes;
    }

    public Integer getDeliveryWorkingTimeMinutes() {
        return deliveryWorkingTimeMinutes;
    }

    public void setDeliveryWorkingTimeMinutes(Integer deliveryWorkingTimeMinutes) {
        this.deliveryWorkingTimeMinutes = deliveryWorkingTimeMinutes;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getOpeningTimeIso() {
        return openingTimeIso;
    }

    public void setOpeningTimeIso(String openingTimeIso) {
        this.openingTimeIso = openingTimeIso;
    }

    public Boolean getSendsOnItsWayNotifications() {
        return sendsOnItsWayNotifications;
    }

    public void setSendsOnItsWayNotifications(Boolean sendsOnItsWayNotifications) {
        this.sendsOnItsWayNotifications = sendsOnItsWayNotifications;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<CuisineType> getCuisineTypes() {
        return cuisineTypes;
    }

    public void setCuisineTypes(List<CuisineType> cuisineTypes) {
        this.cuisineTypes = cuisineTypes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsOpenNow() {
        return isOpenNow;
    }

    public void setIsOpenNow(Boolean isOpenNow) {
        this.isOpenNow = isOpenNow;
    }

    public Boolean getIsPremier() {
        return isPremier;
    }

    public void setIsPremier(Boolean isPremier) {
        this.isPremier = isPremier;
    }

    public Boolean getIsSponsored() {
        return isSponsored;
    }

    public void setIsSponsored(Boolean isSponsored) {
        this.isSponsored = isSponsored;
    }

    public Integer getSponsoredPosition() {
        return sponsoredPosition;
    }

    public void setSponsoredPosition(Integer sponsoredPosition) {
        this.sponsoredPosition = sponsoredPosition;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsTemporarilyOffline() {
        return isTemporarilyOffline;
    }

    public void setIsTemporarilyOffline(Boolean isTemporarilyOffline) {
        this.isTemporarilyOffline = isTemporarilyOffline;
    }

    public String getReasonWhyTemporarilyOffline() {
        return reasonWhyTemporarilyOffline;
    }

    public void setReasonWhyTemporarilyOffline(String reasonWhyTemporarilyOffline) {
        this.reasonWhyTemporarilyOffline = reasonWhyTemporarilyOffline;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public Boolean getIsCloseBy() {
        return isCloseBy;
    }

    public void setIsCloseBy(Boolean isCloseBy) {
        this.isCloseBy = isCloseBy;
    }

    public Boolean getIsHalal() {
        return isHalal;
    }

    public void setIsHalal(Boolean isHalal) {
        this.isHalal = isHalal;
    }

    public Boolean getIsTestRestaurant() {
        return isTestRestaurant;
    }

    public void setIsTestRestaurant(Boolean isTestRestaurant) {
        this.isTestRestaurant = isTestRestaurant;
    }

    public Integer getDefaultDisplayRank() {
        return defaultDisplayRank;
    }

    public void setDefaultDisplayRank(Integer defaultDisplayRank) {
        this.defaultDisplayRank = defaultDisplayRank;
    }

    public Boolean getIsOpenNowForDelivery() {
        return isOpenNowForDelivery;
    }

    public void setIsOpenNowForDelivery(Boolean isOpenNowForDelivery) {
        this.isOpenNowForDelivery = isOpenNowForDelivery;
    }

    public Boolean getIsOpenNowForCollection() {
        return isOpenNowForCollection;
    }

    public void setIsOpenNowForCollection(Boolean isOpenNowForCollection) {
        this.isOpenNowForCollection = isOpenNowForCollection;
    }

    public Integer getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(Integer ratingStars) {
        this.ratingStars = ratingStars;
    }

    public List<Logo> getLogo() {
        return logo;
    }

    public void setLogo(List<Logo> logo) {
        this.logo = logo;
    }

    public List<Object> getDeals() {
        return deals;
    }

    public void setDeals(List<Object> deals) {
        this.deals = deals;
    }

    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

}
