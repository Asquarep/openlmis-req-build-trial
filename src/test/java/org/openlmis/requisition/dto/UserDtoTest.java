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

package org.openlmis.requisition.dto;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.UUID;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.openlmis.requisition.testutils.DtoGenerator;
import org.openlmis.requisition.testutils.UserDtoDataBuilder;

@SuppressWarnings("PMD.TooManyMethods")
public class UserDtoTest {

  private UserDto userDto;

  private RoleAssignmentDto homeFacilitySupervisionRole;
  private RoleAssignmentDto supervisorySupervisionRole;

  @Before
  public void setUp() {
    homeFacilitySupervisionRole = new RoleAssignmentDto(
        UUID.randomUUID(), UUID.randomUUID(), null, null);
    supervisorySupervisionRole = new RoleAssignmentDto(
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null);
    RoleAssignmentDto anotherSupervisorySupervisionRole = new RoleAssignmentDto(
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null);

    userDto = new UserDtoDataBuilder()
        .withUsername("jdoe")
        .withoutFirstName()
        .withoutLastName()
        .withRoleAssignment(homeFacilitySupervisionRole)
        .withRoleAssignment(supervisorySupervisionRole)
        .withRoleAssignment(anotherSupervisorySupervisionRole)
        .build();
  }

  @Test
  public void shouldPrintNameAsFirstLastName() {
    userDto.setFirstName("John");
    userDto.setLastName("Doe");

    assertThat(userDto.printName()).isEqualTo("John Doe");
  }

  @Test
  public void shouldPrintNameAsOnlyFirstName() {
    userDto.setFirstName("John");

    assertThat(userDto.printName()).isEqualTo("John");
  }

  @Test
  public void shouldPrintNameAsOnlyLastName() {
    userDto.setLastName("Doe");

    assertThat(userDto.printName()).isEqualTo("Doe");
  }

  @Test
  public void shouldPrintNameAsUsername() {
    assertThat(userDto.printName()).isEqualTo("jdoe");
  }

  @Test
  public void equalsContract() {
    RoleAssignmentDto roleAssignments = DtoGenerator.of(RoleAssignmentDto.class);

    EqualsVerifier
        .forClass(UserDto.class)
        .withPrefabValues(Set.class, emptySet(), singleton(roleAssignments))
        .withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS) // fields in DTO cannot be final
        .verify();
  }

  @Test
  public void hasSupervisorySupervisionRoleShouldReturnTrueIfUserHasCorrectRole() {
    assertThat(userDto.hasSupervisorySupervisionRole(supervisorySupervisionRole.getRoleId(),
        supervisorySupervisionRole.getProgramId(),
        supervisorySupervisionRole.getSupervisoryNodeId()))
        .isTrue();
  }

  @Test
  public void hasSupervisorySupervisionRoleShouldReturnFalseIfUserHasNoCorrectRole() {
    assertThat(userDto
        .hasSupervisorySupervisionRole(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
        .isFalse();
  }

  @Test
  public void hasHomeFacilitySupervisionRoleShouldReturnTrueIfUserHasCorrectRole() {
    assertThat(userDto.hasHomeFacilitySupervisionRole(homeFacilitySupervisionRole.getRoleId(),
        homeFacilitySupervisionRole.getProgramId(), userDto.getHomeFacilityId()))
        .isTrue();
  }

  @Test
  public void hasHomeFacilitySupervisionRoleShouldReturnFalseIfUserHasNoHomeFacility() {
    userDto.setHomeFacilityId(null);

    assertThat(userDto.hasHomeFacilitySupervisionRole(homeFacilitySupervisionRole.getRoleId(),
        homeFacilitySupervisionRole.getProgramId(), UUID.randomUUID()))
        .isFalse();
  }

  @Test
  public void hasHomeFacilitySupervisionRoleShouldReturnFalseIfHomeFacilityNotMatch() {
    assertThat(userDto.hasHomeFacilitySupervisionRole(homeFacilitySupervisionRole.getRoleId(),
        homeFacilitySupervisionRole.getProgramId(), UUID.randomUUID()))
        .isFalse();
  }

  @Test
  public void hasHomeFacilitySupervisionRoleShouldReturnFalseIfUserHasNoCorrectRole() {
    assertThat(userDto.hasHomeFacilitySupervisionRole(UUID.randomUUID(),
        UUID.randomUUID(), UUID.randomUUID()))
        .isFalse();
  }
}
