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

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.openlmis.requisition.domain.requisition.Requisition;
import org.openlmis.requisition.dto.BasicRequisitionDto;
import org.openlmis.requisition.dto.FacilityDto;
import org.openlmis.requisition.dto.ProgramDto;
import org.openlmis.requisition.dto.RequisitionWithSupplyingDepotsDto;
import org.openlmis.requisition.dto.SupplyLineDto;
import org.openlmis.requisition.repository.RequisitionRepository;
import org.openlmis.requisition.service.RequestParameters;
import org.openlmis.requisition.service.referencedata.FacilityReferenceDataService;
import org.openlmis.requisition.service.referencedata.ProgramReferenceDataService;
import org.openlmis.requisition.service.referencedata.SupplyLineReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequisitionForConvertBuilder {

  @Autowired
  private RequisitionRepository requisitionRepository;

  @Autowired
  private FacilityReferenceDataService facilityReferenceDataService;

  @Autowired
  private ProgramReferenceDataService programReferenceDataService;

  @Autowired
  private BasicRequisitionDtoBuilder basicRequisitionDtoBuilder;

  @Autowired
  private SupplyLineReferenceDataService supplyLineReferenceDataService;

  /**
   * Builds representation of the requisitions that are ready for converting to an order,
   * containing supplying facilities that the user has rights to.
   *
   * @param requisitions          the list of requisitions to build for
   * @param userManagedFacilities UUIDs of supplying depots the user has rights to
   * @return a list of requisition with supplying depots representation
   */
  public List<RequisitionWithSupplyingDepotsDto> buildRequisitions(List<Requisition> requisitions,
      Set<UUID> userManagedFacilities, List<SupplyLineDto> supplyLines) {

    if (supplyLines == null) {
      supplyLines = supplyLineReferenceDataService.getPage(RequestParameters.init()).getContent();
    }

    Map<UUID, FacilityDto> facilities = getFacilities(requisitions, userManagedFacilities);
    Map<UUID, ProgramDto> programs = getPrograms(requisitions);
    Map<UUID, UUID> supervisoryNodeSupplyingFacilityPairs = supplyLines.stream()
        .collect(toMap(SupplyLineDto::getSupervisoryNode, SupplyLineDto::getSupplyingFacility));

    List<RequisitionWithSupplyingDepotsDto> responseList = new ArrayList<>();
    for (Requisition requisition : requisitions) {
      BasicRequisitionDto requisitionDto = basicRequisitionDtoBuilder.build(requisition,
          facilities.get(requisition.getFacilityId()),
          programs.get(requisition.getProgramId()));

      responseList.add(new RequisitionWithSupplyingDepotsDto(
          requisitionDto,
          singletonList(facilities.get(
              supervisoryNodeSupplyingFacilityPairs.get(requisition.getSupervisoryNodeId())))));
    }

    return responseList;
  }

  /**
   * Retrieves available supplying depots for given requisition.
   *
   * @param requisitionId id of requisition to find facilities for
   * @return list of facilities
   */
  public List<FacilityDto> getAvailableSupplyingDepots(UUID requisitionId) {
    return getAvailableSupplyingDepotsForRequisition(requisitionRepository.findOne(requisitionId));
  }

  private List<FacilityDto> getAvailableSupplyingDepotsForRequisition(Requisition requisition) {
    Collection<FacilityDto> facilityDtos = facilityReferenceDataService
        .searchSupplyingDepots(requisition.getProgramId(), requisition.getSupervisoryNodeId());

    return Lists.newArrayList(facilityDtos);
  }

  private Map<UUID, FacilityDto> getFacilities(List<Requisition> requisitions,
      Set<UUID> userManagedFacilities) {
    Set<UUID> facilityIds = new HashSet<>(userManagedFacilities);
    requisitions.stream()
        .map(Requisition::getFacilityId)
        .forEach(facilityIds::add);

    return facilityReferenceDataService.search(facilityIds).stream()
        .collect(toMap(FacilityDto::getId, facility -> facility));
  }

  private Map<UUID, ProgramDto> getPrograms(List<Requisition> requisitions) {
    Set<UUID> programIds = requisitions.stream()
        .map(Requisition::getProgramId)
        .collect(toSet());

    return programReferenceDataService.search(programIds).stream()
        .collect(toMap(ProgramDto::getId, program -> program));
  }
}
