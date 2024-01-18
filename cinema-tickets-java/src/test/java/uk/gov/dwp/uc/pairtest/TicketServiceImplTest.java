package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {


  @InjectMocks
  TicketServiceImpl ticketServiceImpl;
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  @Before
  public void setUp(){
       ticketServiceImpl = new TicketServiceImpl();
    }
  @Test
    public void purchaseTicketsOK1(){
    ticketServiceImpl.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2));
  }

  @Test
  public void purchaseTicketsOK2(){
    ticketServiceImpl.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),new TicketTypeRequest(null, 2));
  }

  @Test
  public void testPurchaseTickets1() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(null, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -1));
  }

  @Test
  public void testPurchaseTickets2() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(null, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
  }

  @Test
  public void testPurchaseTickets3() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(-1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
  }

  @Test
  public void testPurchaseTickets4() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(1L, new TicketTypeRequest(null, 1));
  }


  @Test
  public void testPurchaseTickets5() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
  }


  @Test
  public void testPurchaseTickets6() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));
  }

  @Test
  public void testPurchaseTickets7() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0));
  }


  @Test
  public void testPurchaseTickets8() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(1L, (TicketTypeRequest) null);
  }


  @Test
  public void testPurchaseTickets9() throws InvalidPurchaseException {
    thrown.expect(InvalidPurchaseException.class);
    ticketServiceImpl.purchaseTickets(1L);
  }

}
