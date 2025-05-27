package org.example.bilabonnement_gruppe1.repository;


import org.example.bilabonnement_gruppe1.model.DamageReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Repository
public class DamageReportRepository {

    @Autowired
    private DataSource dataSource;

}