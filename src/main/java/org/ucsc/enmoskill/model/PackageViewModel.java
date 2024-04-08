package org.ucsc.enmoskill.model;

import java.util.List;

public class PackageViewModel {
    Package packageModel;
    List<PackagePricing> pricings;
    ProfileModel profileModel;

    /// REVIEW DATA GOES HERE

    public PackageViewModel(){}

    public PackageViewModel(Package packageModel, List<PackagePricing> pricings, ProfileModel profileModel){
        this.packageModel = packageModel;
        this.pricings = pricings;
        this.profileModel = profileModel;
    }

    public void setPackageModel(Package packageModel) {
        this.packageModel = packageModel;
    }

    public void setPricings(List<PackagePricing> pricings) {
        this.pricings = pricings;
    }

    public void setProfileModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public Package getPackageModel() {
        return packageModel;
    }

    public List<PackagePricing> getPricings() {
        return pricings;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }
}

