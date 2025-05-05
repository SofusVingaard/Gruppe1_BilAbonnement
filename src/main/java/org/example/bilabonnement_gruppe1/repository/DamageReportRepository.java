package org.example.bilabonnement_gruppe1.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class DamageReportRepository {

    @Autowired
    private DataSource dataSource;

}
