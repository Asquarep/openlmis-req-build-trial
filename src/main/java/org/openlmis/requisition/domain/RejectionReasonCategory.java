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

package org.openlmis.requisition.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javers.core.metamodel.annotation.TypeName;

@Entity
@Table(name = "rejection_reason_category")
@NoArgsConstructor
@TypeName("RejectionReasonCategory")
@SuppressWarnings({"PMD.UnusedPrivateField"})
public class RejectionReasonCategory extends BaseEntity {

  private static final String TEXT = "text";

  @Column(nullable = false, unique = true, columnDefinition = TEXT)
  @Getter
  private String name;

  @Column(nullable = false, unique = true, columnDefinition = TEXT)
  @Getter
  @Setter
  private String code;


  public RejectionReasonCategory(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public void updateFrom(RejectionReasonCategory rejectionReasonsCategory) {
    this.name = rejectionReasonsCategory.getName();
    this.code = rejectionReasonsCategory.getCode();
  }

  /**
   * Static factory method for constructing a new rejection reason category with a name and code.
   *
   * @param name rejection reason category name
   * @param code rejection reason category  code
   */
  public static RejectionReasonCategory newRejectionReasonCategory(String name, String code) {
    return new RejectionReasonCategory(name, code);
  }

  /**
   * Static factory method for constructing a new rejection reason category using an importer (DTO).
   *
   * @param importer the rejection reason category importer (DTO)
   */
  public static RejectionReasonCategory newRejectionReasonCategory(
          RejectionReasonCategory.Importer importer) {
    RejectionReasonCategory newRejectionReasonCategory =
            new RejectionReasonCategory(importer.getName(), importer.getCode());
    newRejectionReasonCategory.id = importer.getId();
    newRejectionReasonCategory.code = importer.getCode();

    return newRejectionReasonCategory;
  }

  /**
   * Export this object to the specified exporter (DTO).
   *
   * @param exporter exporter to export to
   */
  public void export(RejectionReasonCategory.Exporter exporter) {
    exporter.setId(id);
    exporter.setName(name);
    exporter.setCode(code);
  }

  public interface Exporter {
    void setId(UUID id);

    void setName(String name);

    void setCode(String code);
  }

  public interface Importer {
    UUID getId();

    String getName();

    String getCode();
  }
}
