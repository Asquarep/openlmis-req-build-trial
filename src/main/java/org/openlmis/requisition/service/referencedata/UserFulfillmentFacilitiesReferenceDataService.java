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

package org.openlmis.requisition.service.referencedata;

import org.openlmis.requisition.dto.FacilityDto;
import org.openlmis.requisition.service.RequestParameters;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class UserFulfillmentFacilitiesReferenceDataService extends FacilityReferenceDataService {

  private static final XLogger XLOGGER = XLoggerFactory.getXLogger(
      UserFulfillmentFacilitiesReferenceDataService.class
  );

  @Override
  protected String getUrl() {
    return "/api/users/";
  }

  /**
   * Retrieves all the facilities that the given user has fulfillment rights for.
   *
   * @param userUuid the UUID of the user to check for
   * @param rightUuid the UUID of the right to check for
   * @return a collection of facilities the user has fulfillment rights for
   */
  public Collection<FacilityDto> getFulfillmentFacilities(UUID userUuid, UUID rightUuid) {
    XLOGGER.entry(userUuid, rightUuid);

    Collection<FacilityDto> facilities = findAll(
        userUuid + "/fulfillmentFacilities",
        RequestParameters.init().set("rightId", rightUuid)
    );

    XLOGGER.exit(facilities);
    return facilities;
  }
}
