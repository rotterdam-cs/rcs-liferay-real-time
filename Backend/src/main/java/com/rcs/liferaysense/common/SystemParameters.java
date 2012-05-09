package com.rcs.liferaysense.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author ggenovese
 */
@Service
public class SystemParameters {

//    @Value(value="${defaultLocale}")
//    private String defaultLocale;
//    
//    @Value(value="${registrationFirstPageTag}")
//    private String registrationFirstPageTag;
//    
//    @Value(value="${confirmationLinkEmailContent}")
//    private String confirmationLinkEmailContent;
//    
//    @Value(value="${dataNurseEmail}")
//    private String dataNurseEmail;
//
//    @Value(value="${dataNurseEmailContent}")
//    private String dataNurseEmailContent;
//    
//    @Value(value="${ivitalityMainEmail}")
//    private String ivitalityMainEmail;
//
//    @Value(value="${registrationFourthPageTag}")
//    private String registrationFourthPageTag;
//
//    @Value(value="${question1Id}")
//    private String question1Id;
//    
//    @Value(value="${question2Id}")
//    private String question2Id;
//    
//    @Value(value="${question3Id}")
//    private String question3Id;
//    
//    @Value(value="${question4Id}")
//    private String question4Id;
//    
//    @Value(value="${question5Id}")
//    private String question5Id;
//    
//    @Value(value="${question6Id}")
//    private String question6Id;
//    
//    @Value(value="${companyId}")
//    private String companyId;
//    
//    @Value(value="${participantHelpPage}")
//    private String participantHelpPage;
//
//    @Value(value="${participantInformationPage}")
//    private String participantInformationPage;
//    
//    @Value(value="${physicianId}")
//    private String physicianId;
//    
//    @Value(value="${notifyEmailTag}")
//    private String notifyEmailTag;
//    
//    @Value(value="${reportTablesZebraStyles}")
//    private String reportTablesZebraStyles;
//    
//    @Value(value="${reportMedicationTableRowTemplate}")
//    private String reportMedicationTableRowTemplate;
//    
//    @Value(value="${reportStudyTableRowTemplate}")
//    private String reportStudyTableRowTemplate;
//    
//    @Value(value="${bloodPressureSensorId}")
//    private String bloodPressureSensorId;
//    
//    @Value("${commonSense.managerUser}")
//    private String senseManagerUser;
//
//    @Value("${commonSense.managerPassword}")
//    private String senseManagerPassword;
//    
//    @Value("${commonSense.baseurl}")
//    private String commonSenseBaseUrl;
//    
//    @Value("${sensor.bloodpressure}")
//    private String sensorNameBloodPressure;
//    
//    @Value("${sensor.sleep}")
//    private String sensorNameSleep;
//    
//    @Value("${sensor.reaction}")
//    private String sensorNameReaction;
//
//    @Value("${sensor.motionenergy}")
//    private String sensorMotionEnergy;
//    
//    @Value("${CSVParticipantDataHeader}")
//    private String CSVParticipantDataHeader;
//            
//    @Value("${CSVParticipantDataTemplate}")
//    private String CSVParticipantDataTemplate;
//            
//    @Value("${CSVMedicalDataHeader}")
//    private String CSVMedicalDataHeader;
//            
//    @Value("${CSVMedicalDataTemplate}")
//    private String CSVMedicalDataTemplate;
//    
//    @Value("${reminderMessageToParticipantId}")
//    private String reminderMessageToParticipant;
//    
//    @Value("${reminderEmailToParticipantId}")
//    private String reminderEmailToParticipant;
//    
//    @Value("${reminderMessageToPhysicianId}")
//    private String reminderMessageToPhysician;
//
//    @Value("${reminderGroupId}")
//    private String reminderGroupId;
//    
//    @Value("${activityalertsurl}")
//    private String activityAlertsUrl;
//    
//    @Value("${dataalertsurl}")
//    private String dataAlertsUrl;
//    
//    @Value("${sensequitnotificationurl}")
//    private String senseQuitUrlTemplate;
//    
//    @Value(value="${commonSense.export.url}")
//    private String commonSenseExportUrl;
//
//    @Value(value="${sensor.motionenergy.daily}")
//    private String sensorMotionEnergyDaily;
//    
//    @Value("${sensor.bloodpressure.avg}")
//    private String sensorNameBloodPressureAvg;
//    
//    @Value("${sensor.sleep.avg}")
//    private String sensorNameSleepAvg;
//    
//    @Value("${sensor.reaction.avg}")
//    private String sensorNameReactionAvg;  
//
//    @Value("${sensor.motionenergy.daily.avg}")
//    private String sensorMotionEnergyDailyAvg;    
//
//    @Value("${physician.dashboard.activityalerts.count}")
//    private String physicianDashboardActivityAlertsCount;
//
//    @Value("${physician.dashboard.dataalerts.count}")
//    private String physicianDashboardDataAlertsCount;
//    
//    @Value("${activityalerts.hide.ifolderthandays}")
//    private String activityAlertsHideIfOlderThanDays;
//    
//    @Value("${sensor.heartrate}")
//    private String sensorNameHeartRate;
//    
//    @Value("${sensor.abpm}")
//    private String sensorNameABPM;
//    
//    @Value("${sensor.startle}")
//    private String sensorNameStartle;    
//    
//    @Value("${sensor.sleep.device}")
//    private String sensorDeviceSleep;
//    
//    @Value("${sensor.bloodpressure.device}")
//    private String sensorDeviceBloodPressure;
//    
//    @Value("${sensor.reaction.device}")
//    private String sensorDeviceReactionTime;
//    
//    @Value("${sensor.motionenergy.device}")
//    private String sensorDeviceMotionEnergy;
//    
//    @Value("${sensor.motionenergy.daily.device}")
//    private String sensorDeviceMotionEnergyDaily;
//    
//    @Value("${sensor.heartrate.device}")
//    private String sensorDeviceHeartRate;
//    
//    @Value("${sensor.abpm.device}")
//    private String sensorDeviceABPM;
//    
//    @Value("${sensor.startle.device}")
//    private String sensorDeviceStartle;
//    
//    @Value("${mailToPhysicianAfterUserCreationTag}")
//    private String mailToPhysicianAfterUserCreationTag;
//    
//    public String getSensorMotionEnergyDaily() {
//        return sensorMotionEnergyDaily;
//    }
//
//    public void setSensorMotionEnergyDaily(String sensorMotionEnergyDaily) {
//        this.sensorMotionEnergyDaily = sensorMotionEnergyDaily;
//    }
//    
//    public String getSensorMotionEnergy() {
//        return sensorMotionEnergy;
//    }
//
//    public void setSensorMotionEnergy(String sensorMotionEnergy) {
//        this.sensorMotionEnergy = sensorMotionEnergy;
//    }
//    
//    @Value(value="${averageresultsparticipantsensorid}")
//    private String averageResultsParticipantSensorId;
//
//    public int getAverageResultsParticipantSensorId() {
//        return Integer.parseInt(averageResultsParticipantSensorId);
//    }
//    
//    public String getCSVMedicalDataHeader() {
//        return CSVMedicalDataHeader;
//    }
//
//    public String getCSVMedicalDataTemplate() {
//        return CSVMedicalDataTemplate;
//    }
//
//    public String getCSVParticipantDataHeader() {
//        return CSVParticipantDataHeader;
//    }
//
//    public String getCSVParticipantDataTemplate() {
//        return CSVParticipantDataTemplate;
//    }
//
//    public int getBloodPressureSensorId() {
//        return Integer.parseInt(bloodPressureSensorId);
//    }
//
//    public String getCommonSenseBaseUrl() {
//        return commonSenseBaseUrl;
//    }
//
//    public String getCompanyId() {
//        return companyId;
//    }
//
//    public String getConfirmationLinkEmailContent() {
//        return confirmationLinkEmailContent;
//    }
//
//    public String getDataNurseEmail() {
//        return dataNurseEmail;
//    }
//
//    public String getDataNurseEmailContent() {
//        return dataNurseEmailContent;
//    }
//
//    public String getDefaultLocale() {
//        return defaultLocale;
//    }
//
//    public String getIvitalityMainEmail() {
//        return ivitalityMainEmail;
//    }
//
//    public String getNotifyEmailTag() {
//        return notifyEmailTag;
//    }
//
//    public String getParticipantHelpPage() {
//        return participantHelpPage;
//    }
//
//    public String getParticipantInformationPage() {
//        return participantInformationPage;
//    }
//
//    public long getPhysicianId() {
//        return Long.parseLong(physicianId);
//    }
//
//    public int getQuestion1Id() {
//        return Integer.parseInt(question1Id);
//    }
//
//    public int getQuestion2Id() {
//        return Integer.parseInt(question2Id);
//    }
//
//    public int getQuestion3Id() {
//        return Integer.parseInt(question3Id);
//    }
//
//    public int getQuestion4Id() {
//        return Integer.parseInt(question4Id);
//    }
//
//    public int getQuestion5Id() {
//        return Integer.parseInt(question5Id);
//    }
//
//    public int getQuestion6Id() {
//        return Integer.parseInt(question6Id);
//    }
//
//    public String getRegistrationFirstPageTag() {
//        return registrationFirstPageTag;
//    }
//
//    public String getRegistrationFourthPageTag() {
//        return registrationFourthPageTag;
//    }
//
//    public String getReportMedicationTableRowTemplate() {
//        return reportMedicationTableRowTemplate;
//    }
//
//    public String getReportStudyTableRowTemplate() {
//        return reportStudyTableRowTemplate;
//    }
//
//    public String getReportTablesZebraStyles() {
//        return reportTablesZebraStyles;
//    }
//
//    public String getSenseManagerPassword() {
//        return senseManagerPassword;
//    }
//
//    public String getSenseManagerUser() {
//        return senseManagerUser;
//    }
//
//    public String getSensorNameBloodPressure() {
//        return sensorNameBloodPressure;
//    }
//
//    public String getSensorNameReaction() {
//        return sensorNameReaction;
//    }
//
//    public String getSensorNameSleep() {
//        return sensorNameSleep;
//    }
//    
//    public String getReminderEmailToParticipant() {
//        return reminderEmailToParticipant;
//    }
//
//    public String getReminderMessageToParticipant() {
//        return reminderMessageToParticipant;
//    }
//
//    public String getReminderMessageToPhysician() {
//        return reminderMessageToPhysician;
//    }
//
//    public long getReminderGroupId(){
//        return Long.parseLong(reminderGroupId);
//    }
//
//    public String getActivityAlertsUrl() {
//        return activityAlertsUrl;
//    }
//
//
//    public String getDataAlertsUrl() {
//        return dataAlertsUrl;
//    }
//
//    public String getSenseQuitUrlTemplate() {
//        return senseQuitUrlTemplate;
//    }
//
//    public void setSenseQuitUrlTemplate(String senseQuitUrlTemplate) {
//        this.senseQuitUrlTemplate = senseQuitUrlTemplate;
//    }
//
//    public String getCommonSenseExportUrl() {
//        return commonSenseExportUrl;
//    }
//
//    public void setCommonSenseExportUrl(String commonSenseExportUrl) {
//        this.commonSenseExportUrl = commonSenseExportUrl;
//    }
//
//    public String getSensorMotionEnergyDailyAvg() {
//        return sensorMotionEnergyDailyAvg;
//    }
//
//    public String getSensorNameBloodPressureAvg() {
//        return sensorNameBloodPressureAvg;
//    }
//
//    public String getSensorNameReactionAvg() {
//        return sensorNameReactionAvg;
//    }
//
//    public String getSensorNameSleepAvg() {
//        return sensorNameSleepAvg;
//    }
//
//    public int getPhysicianDashboardActivityAlertsCount() {
//        return Integer.parseInt(physicianDashboardActivityAlertsCount);
//    }
//    
//    public int getPhysicianDashboardDataAlertsCount() {
//        return Integer.parseInt(physicianDashboardDataAlertsCount);
//    }
//
//    public int getActivityAlertsHideIfOlderThanDays() {
//        return Integer.parseInt(activityAlertsHideIfOlderThanDays);
//    }
//
//    public String getSensorNameHeartRate() {
//        return sensorNameHeartRate;
//    }
//
//    public String getSensorNameABPM() {
//        return sensorNameABPM;
//    }
//
//    public String getSensorNameStartle() {
//        return sensorNameStartle;
//    }
//
//    public String getSensorDeviceABPM() {
//        return sensorDeviceABPM;
//    }
//
//    public String getSensorDeviceBloodPressure() {
//        return sensorDeviceBloodPressure;
//    }
//
//    public String getSensorDeviceHeartRate() {
//        return sensorDeviceHeartRate;
//    }
//
//    public String getSensorDeviceMotionEnergy() {
//        return sensorDeviceMotionEnergy;
//    }
//
//    public String getSensorDeviceMotionEnergyDaily() {
//        return sensorDeviceMotionEnergyDaily;
//    }
//
//    public String getSensorDeviceReactionTime() {
//        return sensorDeviceReactionTime;
//    }
//
//    public String getSensorDeviceSleep() {
//        return sensorDeviceSleep;
//    }
//
//    public String getSensorDeviceStartle() {
//        return sensorDeviceStartle;
//    }
//
//    public String getMailToPhysicianAfterUserCreationTag() {
//        return mailToPhysicianAfterUserCreationTag;
//    }

}
