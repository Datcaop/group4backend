package org.fai.study.projectsem4.service.interfaces;

import org.fai.study.projectsem4.entity.DTOs.ShippingRequestDTO;
import org.fai.study.projectsem4.entity.DTOs.ShippingResponseDTO;
import org.fai.study.projectsem4.entity.Shipping;
import org.springframework.web.multipart.MultipartFile;

public interface IShippingService {
    ShippingResponseDTO addShipping(ShippingRequestDTO requestDTO);
    String shipperChangeOrderStatusIntoShipping(Integer shippingId);
    String shipperChangeOrderStatusIntoFinished(Integer shippingId, MultipartFile image);
    Shipping shipperChangeOrderStatusIntoNotAccepted(Integer shippingId);
}
