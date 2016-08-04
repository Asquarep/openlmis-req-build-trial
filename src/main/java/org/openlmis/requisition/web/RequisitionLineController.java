package org.openlmis.requisition.web;

import org.openlmis.product.domain.Product;
import org.openlmis.requisition.domain.Requisition;
import org.openlmis.requisition.domain.RequisitionLine;
import org.openlmis.requisition.service.RequisitionLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RepositoryRestController
public class RequisitionLineController {

  @Autowired
  private RequisitionLineService requisitionLineService;

  /**
   * Returns all requisition lines with matched parameters.
   */
  @RequestMapping(value = "/requisitionLines/search", method = RequestMethod.GET)
  public ResponseEntity<?> searchRequisitionLines(
      @RequestParam(value = "requisition", required = false) Requisition requisition, //TODO true
      @RequestParam(value = "product", required = true) Product product) {
    List<RequisitionLine> result = requisitionLineService
        .searchRequisitionLines(requisition, product);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
