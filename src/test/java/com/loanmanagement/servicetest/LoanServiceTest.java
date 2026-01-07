package com.loanmanagement.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.model.Loan;
import com.loanmanagement.service.LoanService;

class LoanServiceTest {

    private LoanService loanService;
    private LoanDao loanDao;

    @BeforeEach
    void setup() throws Exception {

        loanService = new LoanService();
        loanDao = mock(LoanDao.class);

        
        Field field = LoanService.class.getDeclaredField("loanDao");
        field.setAccessible(true);
        field.set(loanService, loanDao);
    }

    @Test
    void testCreateLoan() {

        Loan loan = new Loan();
        loan.setLoanAccountNo("LN123");
        loan.setPrincipalAmount(BigDecimal.valueOf(50000));

        loanService.createLoan(loan);

        verify(loanDao).insert(loan);
    }

    @Test
    void testGetAllLoans() {

        List<Loan> loans = List.of(new Loan());
        when(loanDao.findAll()).thenReturn(loans);

        List<Loan> result = loanService.getAllLoans();

        assertEquals(loans, result);
        verify(loanDao).findAll();
    }

    @Test
    void testUpdateLoan() {

        Loan loan = new Loan();
        loan.setId(1L);

        loanService.updateLoan(loan);

        verify(loanDao).update(loan);
    }

    @Test
    void testDeleteLoan() {

        loanService.deleteLoan(1L);

        verify(loanDao).delete(1L);
    }
}
