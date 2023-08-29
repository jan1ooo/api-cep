package com.jan1ooo.apicep.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddressStatus {
    public static final Long DEFAULT_ID = 1L;

    @Id
    private Long id;
    private Status status;
}
