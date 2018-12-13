/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.requisition.web;

import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.openlmis.requisition.i18n.MessageKeys.ERROR_INVALID_REQUISITION_STATUS;
import static org.openlmis.requisition.i18n.MessageKeys.ERROR_SEARCH_INVALID_PARAMS;

import be.joengenduvel.java.verifiers.ToStringVerifier;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openlmis.requisition.domain.requisition.RequisitionStatus;
import org.openlmis.requisition.dto.ToStringContractTest;
import org.openlmis.requisition.exception.ValidationMessageException;
import org.springframework.util.LinkedMultiValueMap;

@SuppressWarnings("PMD.TooManyMethods")
public class RequisitionSearchParamsTest extends ToStringContractTest<RequisitionSearchParams> {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private static final String FACILITY = "facility";
  private static final String PROGRAM = "program";
  private static final String INITIATED_DATE_FROM = "initiatedDateFrom";
  private static final String INITIATED_DATE_TO = "initiatedDateTo";
  private static final String PROCESSING_PERIOD = "processingPeriod";
  private static final String SUPERVISORY_NODE = "supervisoryNode";
  private static final String REQUISITION_STATUS = "requisitionStatus";
  private static final String EMERGENCY = "emergency";
  private static final String MODIFIED_DATE_FROM = "modifiedDateFrom";
  private static final String MODIFIED_DATE_TO = "modifiedDateTo";

  private LinkedMultiValueMap<String, String> queryMap;
  private UUID id = UUID.randomUUID();
  private String dateString = "2018-06-28";
  private LocalDate date = LocalDate.of(2018, 6, 28);
  private String dateTimeString = "2018-06-28T17:25:46Z";
  private ZonedDateTime dateTime = ZonedDateTime.of(2018,6,28,17,25,46,0, ZoneId.of("Z"));

  @Before
  public void setUp() {
    queryMap = new LinkedMultiValueMap<>();
  }

  @Test
  public void shouldGetFacilityValueFromParameters() {
    queryMap.add(FACILITY, id.toString());
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(id, params.getFacility());
  }

  @Test
  public void shouldGetNullIfMapHasNoFacilityProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getFacility());
  }

  @Test
  public void shouldGetProgramValueFromParameters() {
    queryMap.add(PROGRAM, id.toString());
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(id, params.getProgram());
  }

  @Test
  public void shouldGetNullIfMapHasNoProgramProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getProgram());
  }

  @Test
  public void shouldGetInitiatedDateFromValueFromParameters() {
    queryMap.add(INITIATED_DATE_FROM, dateString);
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(date, params.getInitiatedDateFrom());
  }

  @Test
  public void shouldGetNullIfMapHasNoInitiatedDateFromProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getInitiatedDateFrom());
  }

  @Test
  public void shouldGetInitiatedDateToValueFromParameters() {
    queryMap.add(INITIATED_DATE_TO, dateString);
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(date, params.getInitiatedDateTo());
  }

  @Test
  public void shouldGetNullIfMapHasNoInitiatedDateToProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getInitiatedDateTo());
  }

  @Test
  public void shouldGetProcessingPeriodValueFromParameters() {
    queryMap.add(PROCESSING_PERIOD, id.toString());
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(id, params.getProcessingPeriod());
  }

  @Test
  public void shouldGetNullIfMapHasNoProcessingPeriodProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getProcessingPeriod());
  }

  @Test
  public void shouldGetSupervisoryNodeValueFromParameters() {
    queryMap.add(SUPERVISORY_NODE, id.toString());
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(id, params.getSupervisoryNode());
  }

  @Test
  public void shouldGetNullIfMapHasNoSupervisoryNodeProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getSupervisoryNode());
  }

  @Test
  public void shouldGetRequisitionStatusValueFromParameters() {
    queryMap.add(REQUISITION_STATUS, RequisitionStatus.APPROVED.toString());
    queryMap.add(REQUISITION_STATUS, RequisitionStatus.AUTHORIZED.toString());
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertThat(params.getRequisitionStatuses(),
        hasItems(RequisitionStatus.APPROVED,  RequisitionStatus.AUTHORIZED));
  }

  @Test
  public void shouldGetEmptySetIfMapHasNoRequisitionStatusProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(emptySet(), params.getRequisitionStatuses());
  }

  @Test
  public void shouldThrowExceptionIfRequisitionStatusIsNotValid() {
    exception.expect(ValidationMessageException.class);
    exception.expectMessage(ERROR_INVALID_REQUISITION_STATUS);

    queryMap.add(REQUISITION_STATUS, RequisitionStatus.APPROVED.toString());
    queryMap.add(REQUISITION_STATUS, "SOME_STATUS");
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    params.getRequisitionStatuses();
  }

  @Test
  public void shouldGetEmergencyValueFromParameters() {
    queryMap.add(EMERGENCY, "true");
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertTrue(params.isEmergency());

    queryMap.set(EMERGENCY, "false");
    params = new RequisitionSearchParams(queryMap);

    assertFalse(params.isEmergency());
  }

  @Test
  public void shouldGetNullIfMapHasNoEmergencyProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.isEmergency());
  }

  @Test
  public void shouldGetStartModifiedDateValueFromParameters() {
    queryMap.add(MODIFIED_DATE_FROM, dateTimeString);
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(dateTime, params.getModifiedDateFrom());
  }

  @Test
  public void shouldGetNullIfMapHasNoStartModifiedDateProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getModifiedDateFrom());
  }

  @Test
  public void shouldGetEndModifiedDateValueFromParameters() {
    queryMap.add(MODIFIED_DATE_TO, dateTimeString);
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertEquals(dateTime, params.getModifiedDateTo());
  }

  @Test
  public void shouldGetNullIfMapHasNoEndModifiedDateToProperty() {
    RequisitionSearchParams params = new RequisitionSearchParams(queryMap);

    assertNull(params.getModifiedDateTo());
  }

  @Test
  public void shouldThrowExceptionIfThereIsUnknownParameterInParameters() {
    exception.expect(ValidationMessageException.class);
    exception.expectMessage(ERROR_SEARCH_INVALID_PARAMS);

    queryMap.add("some-param", "some-value");
    new RequisitionSearchParams(queryMap);
  }

  @Override
  protected Class<RequisitionSearchParams> getTestClass() {
    return RequisitionSearchParams.class;
  }

  @Override
  protected Optional<RequisitionSearchParams> getInstance() {
    return Optional.of(new RequisitionSearchParams(queryMap));
  }

  @Override
  protected void prepare(ToStringVerifier<RequisitionSearchParams> verifier) {
    verifier.ignore("FACILITY", "PROGRAM", "INITIATED_DATE_FROM", "INITIATED_DATE_TO",
        "MODIFIED_DATE_FROM", "MODIFIED_DATE_TO", "PROCESSING_PERIOD",
        "SUPERVISORY_NODE", "REQUISITION_STATUS", "EMERGENCY", "ALL_PARAMETERS");
  }
}
