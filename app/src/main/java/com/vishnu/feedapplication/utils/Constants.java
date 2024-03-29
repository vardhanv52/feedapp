package com.vishnu.feedapplication.utils;

import java.text.SimpleDateFormat;

public class Constants
{
    public static String userPreferencesName = "FeedApplicationUser",
            appPreferencesName = "FeedApplication";

    public static String noNetwork = "No network connection...";

    public static String logout_msg = "Are you sure you want to logout?",
    confirm = "Confirmation",
    exit_msg = "Are you sure you want to EXIT?";

    public static String keyDisplayName = "DisplayName",
    keyEmail = "Email",
    keyCredentials = "Credentials",
    keyCredEmail = "Credentials_Email",
    keyCredPwd = "Credentials_Pwd";

    public static String valTrue = "TRUE",
    valFalse = "FALSE";

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd hh:mm a");

    public static int paginationCount = 10;

}
