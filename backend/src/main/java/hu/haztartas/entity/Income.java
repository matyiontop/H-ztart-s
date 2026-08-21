package hu.haztartas.entity;

import hu.haztartas.entity.enums.RecurrenceFrequency;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RecurrenceFrequency frequency = RecurrenceFrequency.MONTHLY;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "received_date", nullable = false)
    private LocalDate receivedDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = true;

    @Column(name = "duration_months")
    private Integer durationMonths = 1; // 1 = egyszeri, 2, 3.. = X hónapig, null vagy 0 = állandó

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Income() {}

    public Income(String title, BigDecimal amount, RecurrenceFrequency frequency, Category category, LocalDate receivedDate, String description, boolean isRecurring) {
        this(title, amount, frequency, category, receivedDate, description, isRecurring, isRecurring ? null : 1);
    }

    public Income(String title, BigDecimal amount, RecurrenceFrequency frequency, Category category, LocalDate receivedDate, String description, boolean isRecurring, Integer durationMonths) {
        this.title = title;
        this.amount = amount;
        this.frequency = frequency != null ? frequency : RecurrenceFrequency.MONTHLY;
        this.category = category;
        this.receivedDate = receivedDate != null ? receivedDate : LocalDate.now();
        this.description = description;
        this.isRecurring = isRecurring;
        this.durationMonths = durationMonths;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public RecurrenceFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(RecurrenceFrequency frequency) {
        this.frequency = frequency;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
