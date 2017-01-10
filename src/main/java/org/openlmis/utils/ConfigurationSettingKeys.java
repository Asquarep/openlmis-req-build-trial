package org.openlmis.utils;

public abstract class ConfigurationSettingKeys {

  public static final String REQUISITION_EMAIL_NOREPLY
      = "requisition.email.noreply";
  public static final String REQUISITION_EMAIL_CONVERT_TO_ORDER_SUBJECT
      = "requisition.email.convert-to-order.subject";
  public static final String REQUISITION_EMAIL_CONVERT_TO_ORDER_CONTENT
      = "requisition.email.convert-to-order.content";

  private ConfigurationSettingKeys() {
    throw new UnsupportedOperationException();
  }

}