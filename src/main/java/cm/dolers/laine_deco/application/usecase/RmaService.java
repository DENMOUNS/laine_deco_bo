package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service pour RMA (Return Merchandise Authorization)
 */
public interface RmaService {
    RmaResponse createRma(CreateRmaRequest request);
    RmaResponse getRmaById(Long rmaId);
    RmaResponse getRmaByNumber(String rmaNumber);
    Page<RmaResponse> getAllRmas(Pageable pageable);
    Page<RmaResponse> getUserRmas(Long userId, Pageable pageable);
    RmaResponse updateRmaStatus(Long rmaId, String status);
    void approveRma(Long rmaId);
    void rejectRma(Long rmaId);
    void resolveRma(Long rmaId);
}

