package com.endava.appium.framework.objects;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class EndavaGoUiObjects {

    private static final String APP_PACKAGE = "com.endava.rsbgd.app.endavago";

    private static By
            endavaGoLogo,
            signInWithMicrosoftBtn,
            pickAccountButton,
            urlTextField,
            useAnotherAccountButton,
            openSignOutMenuButton,
            signOutAccountButton,
            signOutAndForgetAccountButton,
            seekerProfileButton,
            ownerProfileButton,
            currentSeekerParkingSpotStatusText,
            releaseSeekerParkingSpotButton,
            ownerSearchReleaseButton,
            ownerParkingSpotStatusInfo,
            ownerNameOfParkingSpot,
            appErrorMessage,
            closeAppBtn,
            releaseConfirmationButton,
            cancelButton,
            searchButton,
            snackBarNotification,
            moreInfoButton,
            profileNameField,
            profileEmailField,
            profileImage,
            aboutScreenManual,
            logOutBtn,
            backBtn,
            useAnotherAccBtn,
            inputEmailFieldMicrosoftSignInPage,
            nextBtnMicrosoftSignInPage,
            backBtnMicrosoftSignInPage,
            usernameFieldEndavaFederationSignInPage,
            accountSignOutBtn,
            accountSignOutAndForgetBtn,
            accOptionTab,
            accOptionSignOutTab,
            accOptionSignOutAndForgetTab,
    //added in version 1.0.3
    seekerTapToFindSpotTextMessage,
            seekerYourParkingSpotForTodayTextMessage,
            seekerReleaseButton,
            confirmationMessageText,
            manualInfoButton,
            contactInfoButton,
            aboutInfoButton,
            toolbarTitleField,
            okButton,
            infoButton,
            closeManualButton;


    public By endavaGoLogo (){
        if (endavaGoLogo == null) endavaGoLogo = MobileBy.AndroidUIAutomator("new UiSelector().text(\"EndavaGo\")");
        return endavaGoLogo;
    }

    public By urlTextFieldBuild1Dot0() {
        if (urlTextField == null)
//            urlTextField = MobileBy.id("com.sec.android.app.sbrowser:id/url_bar_text");  //Build 1.0.1
            urlTextField = MobileBy.id("com.android.chrome:id/url_bar");
        return urlTextField;
    }

    public By urlTextFieldBuild1Dot0Dot1() {
        if (urlTextField == null)
//            urlTextField = MobileBy.id("com.sec.android.app.sbrowser:id/url_bar_text");  //Build 1.0.1
            urlTextField = MobileBy.id("com.android.chrome:id/url_bar");  //Build 1.0
        return urlTextField;
    }

    public By signInWithMicrosoftBtn (){
        if (signInWithMicrosoftBtn == null) signInWithMicrosoftBtn = MobileBy.id(APP_PACKAGE + ":id/signInMsButton");
        return signInWithMicrosoftBtn;
    }

    //todo check the concatenation
    public By pickAccBtn (String email){
//        if (pickAccountButton == null) pickAccountButton = MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Sign in\")");
//        if (pickAccountButton == null) pickAccountButton = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign in with Jovan.Penic@endava.com work or school account.\")");
        if (pickAccountButton == null) pickAccountButton = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign in with " + email + " work or school account.\")");

        return pickAccountButton;
    }

    public By openSignOutMenuButton(){
        if (openSignOutMenuButton == null) openSignOutMenuButton = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Open Menu\")");
        return openSignOutMenuButton;
    }

    public By signOutAccountButton(){
        if (signOutAccountButton == null) signOutAccountButton = MobileBy.id("signoutLink");
        return signOutAccountButton;
    }

    public By signOutAndForgetAccountButton(){
        if (signOutAndForgetAccountButton == null) signOutAndForgetAccountButton = MobileBy.id("signoutAndForgetLink");
        return signOutAndForgetAccountButton;
    }

    public By useAnotherAccountButton(){
        if (useAnotherAccountButton == null) useAnotherAccountButton = MobileBy.id("otherTile");
        return useAnotherAccountButton;
    }

    public By seekerProfileButton(){
        if (seekerProfileButton == null) seekerProfileButton = MobileBy.id(APP_PACKAGE + ":id/header");
        return seekerProfileButton;
    }

    public By currentSeekerParkingSpotStatusText(){
        if (currentSeekerParkingSpotStatusText == null) currentSeekerParkingSpotStatusText = MobileBy.id(APP_PACKAGE + ":id/message_container");
        return currentSeekerParkingSpotStatusText;
    }

    public By searchButton(){
        if (searchButton == null) searchButton = MobileBy.id(APP_PACKAGE + ":id/search_button");
        return searchButton;
    }

    public By snackBarNotification(){
        if (snackBarNotification == null) snackBarNotification = MobileBy.id(APP_PACKAGE + ":id/snackbar_text");
        return snackBarNotification;
    }

    public By releaseParkingSpotSeekerButton(){
        if (releaseSeekerParkingSpotButton == null) releaseSeekerParkingSpotButton = MobileBy.id(APP_PACKAGE + ":id/release_button");
        return releaseSeekerParkingSpotButton;
    }

    public By signOutFromProfileScreenButton (){
//        if (logOutBtn == null) logOutBtn = MobileBy.id(APP_PACKAGE + ":id/signOutImageView");  // Build 1.0.3
        if (logOutBtn == null) logOutBtn = MobileBy.id(APP_PACKAGE + ":id/toolbar_signout");    // Build 1.0.5
        return logOutBtn;
    }

    public By backFromProfileScreenButton (){
        if (backBtn == null) backBtn = MobileBy.id(APP_PACKAGE + ":id/toolbar_back");
        return backBtn;
    }

    public By moreInfoButton(){
        if (moreInfoButton == null) moreInfoButton = MobileBy.id(APP_PACKAGE + ":id/more_info_item");
        return moreInfoButton;
    }

    public By releaseConfirmationButton(){
        if (releaseConfirmationButton == null) releaseConfirmationButton = MobileBy.id(APP_PACKAGE + ":id/go_dialog_button_release");
        return releaseConfirmationButton;
    }

    public By seekerCancelReleaseButton(){
        if (cancelButton == null) cancelButton = MobileBy.id(APP_PACKAGE + ":id/go_dialog_button_cancel");
        return cancelButton;
    }

    public By aboutScreenManual(){
        if (aboutScreenManual == null) aboutScreenManual = MobileBy.id(APP_PACKAGE + ":id/manual_title");
        return aboutScreenManual;
    }

    public By profileImage(){
        if (profileImage == null) profileImage = MobileBy.id(APP_PACKAGE + ":id/profileImage");
        return profileImage;
    }

    public By profileUserNameField(){
        if (profileNameField == null) profileNameField = MobileBy.id(APP_PACKAGE + ":id/nameAndLastName");
        return profileNameField;
    }

    public By profileEmailField(){
        if (profileEmailField == null) profileEmailField = MobileBy.id(APP_PACKAGE + ":id/userEmail");
        return profileEmailField;
    }

    public By appErrorMessage() {
        if (appErrorMessage == null)
            appErrorMessage = MobileBy.id("android:id/alertTitle");
        return appErrorMessage;
    }

    public By closeAppBtn() {
        if (closeAppBtn == null)
            closeAppBtn = MobileBy.id("android:id/aerr_close");
        return closeAppBtn;
    }

    public By ownerProfileButton() {
        if (ownerProfileButton == null)
            ownerProfileButton = MobileBy.id(APP_PACKAGE + ":id/user_profile_image");
        //vrv ce ici :id/header, ali nije jedini, vec bar drugi po redu...
        return ownerProfileButton;
    }

    public By ownerSearchReleaseButton(int orderNumberOfTheDay) {
//        if (ownerSearchReleaseButton == null)  //can't have this if statement here, cause when we have for loop in @test, ownerSearchReleaseButton is not null, and it's value remains the same, eg. it is not ==null;
        ownerSearchReleaseButton = MobileBy.xpath("(//android.widget.Button[@resource-id='" + APP_PACKAGE + ":id/pso_button'])[" + orderNumberOfTheDay + "]");
        return ownerSearchReleaseButton;
    }

    public By ownerParkingSpotStatusInfo(int orderNumberOfTheDay) {
//        if (ownerParkingSpotStatusInfo == null)
        ownerParkingSpotStatusInfo = MobileBy.xpath("(//android.widget.TextView[@resource-id='" + APP_PACKAGE + ":id/parking_spot_info'])[" + orderNumberOfTheDay + "]");
        return ownerParkingSpotStatusInfo;
    }

    public By ownerCodeOfParkingSpot() {
        if (ownerNameOfParkingSpot == null)
            ownerNameOfParkingSpot = MobileBy.id(APP_PACKAGE + ":id/pso_parking_spot");
        return ownerNameOfParkingSpot;
    }

    public By useAnotherAccBtn() {
        if (useAnotherAccBtn == null)
            useAnotherAccBtn = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Use another account\")");
        return useAnotherAccBtn;
    }

    public By inputEmailFieldMicrosoftSignInPage() {
        if (inputEmailFieldMicrosoftSignInPage == null)
            inputEmailFieldMicrosoftSignInPage = MobileBy.className("android.widget.EditText");
        return inputEmailFieldMicrosoftSignInPage;
    }

    public By nextBtnMicrosoftSignInPage() {
        if (nextBtnMicrosoftSignInPage == null)
            nextBtnMicrosoftSignInPage = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Next\")");
        return nextBtnMicrosoftSignInPage;
    }

    public By backBtnMicrosoftSignInPage() {
        if (backBtnMicrosoftSignInPage == null)
            backBtnMicrosoftSignInPage = MobileBy.id("idBtn_Back");
        return backBtnMicrosoftSignInPage;
    }

    public By usernameFieldEndavaFederationSignInPage(String text) {
        if (usernameFieldEndavaFederationSignInPage == null)
            usernameFieldEndavaFederationSignInPage = MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + text + "\")");
        return usernameFieldEndavaFederationSignInPage;
    }

    public By accountSignOutBtn() {
        if (accountSignOutBtn == null)
            accountSignOutBtn = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign out\")");
        return accountSignOutBtn;
    }

    public By accountSignOutAndForgetBtn() {
        if (accountSignOutAndForgetBtn == null)
            accountSignOutAndForgetBtn = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign out and forget\")");
        return accountSignOutAndForgetBtn;
    }

    public By accOptionTab(String emailAddress) {
        if (accOptionTab == null)
            accOptionTab = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign in with " + emailAddress + " work or school account.\").childSelector(new UiSelector().text(\"Open menu\"));");
        return accOptionTab;
    }

    public By accOptionSignOutTab() {
        if (accOptionSignOutTab == null)
            accOptionSignOutTab = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign out\")");
        return accOptionSignOutTab;
    }

    public By accOptionSignOutAndForgetTab() {
        if (accOptionSignOutAndForgetTab == null)
            accOptionSignOutAndForgetTab = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign out and forget\")");
        return accOptionSignOutAndForgetTab;
    }

    public By seekerTapToFindSpotBottomTextMessage(){
        if (seekerTapToFindSpotTextMessage == null)
            seekerTapToFindSpotTextMessage = MobileBy.id(APP_PACKAGE + ":id/seeker_search_bottom_message");
        return seekerTapToFindSpotTextMessage;
    }

    public By seekerYourParkingSpotForTodayTopTextMessage(){
        if (seekerYourParkingSpotForTodayTextMessage == null)
            seekerYourParkingSpotForTodayTextMessage = MobileBy.id(APP_PACKAGE + ":id/message_content_top");
        return seekerYourParkingSpotForTodayTextMessage;
    }

    public By confirmationMessageText() {
        if(confirmationMessageText == null)
            confirmationMessageText = MobileBy.id(APP_PACKAGE + ":id/go_dialog_text");
        return confirmationMessageText;
    }

    public By backOneScreenFromManualButton() {
        if (closeManualButton == null)
            closeManualButton = MobileBy.id(APP_PACKAGE + ":id/toolbar_back");
        return closeManualButton;
    }

    public By manualInfoButton(){
        if (manualInfoButton == null) manualInfoButton = MobileBy.id(APP_PACKAGE + ":id/more_info_button");
        return manualInfoButton;
    }
    public By contactInfoButton(){
        if (contactInfoButton == null) contactInfoButton = MobileBy.id(APP_PACKAGE + ":id/contact_us_button");
        return contactInfoButton;
    }
    public By aboutInfoButton(){
        if (aboutInfoButton == null) aboutInfoButton = MobileBy.id(APP_PACKAGE + ":id/about_button");
        return aboutInfoButton;
    }

    public By toolbarTitleField(){
        if (toolbarTitleField == null) toolbarTitleField = MobileBy.id(APP_PACKAGE + ":id/toolbar_title");
        return toolbarTitleField;
    }

    public By okButton (){
        if (okButton == null) okButton = MobileBy.id("android:id/button1");
        return okButton;
    }

    public By cancelButton (){
        if (cancelButton == null) cancelButton = MobileBy.id("android:id/button2");
        return cancelButton;
    }

    public By infoButton (){
        if (infoButton == null) infoButton = MobileBy.id(APP_PACKAGE + ":id/manualImageView");
        return infoButton;
    }

}
