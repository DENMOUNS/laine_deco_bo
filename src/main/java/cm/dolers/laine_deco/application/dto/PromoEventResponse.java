package cm.dolers.laine_deco.application.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
public record PromoEventResponse(Long id, String name, String description, BigDecimal discountPercentage, BigDecimal discountAmount, LocalDate startDate, LocalDate endDate, String status, Instant createdAt) {}
