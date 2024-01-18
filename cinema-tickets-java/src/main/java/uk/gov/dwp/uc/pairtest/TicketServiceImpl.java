package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.Constants;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private static final int ADULT_TICKET_PRICE = 20;
    private static final int CHILD_TICKET_PRICE=10;
    private static final int MAX_PROCESSED_TICKET_LIMIT =20;

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        try{
           if(accountId==null || accountId<0){
               throw new InvalidPurchaseException(Constants.INVALID_ACCOUNT_ID);
           }
           if(ticketTypeRequests==null || ticketTypeRequests.length==0){
               throw new InvalidPurchaseException(Constants.INVALID_TICKET_TYPE_REQUEST);
           }

           int[] totalPayAndSeats = calculateTotalPaymentAndSeats(ticketTypeRequests);

           TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();
           ticketPaymentService.makePayment(accountId,totalPayAndSeats[0]);

           SeatReservationService seatReservationService = new SeatReservationServiceImpl();
           seatReservationService.reserveSeat(accountId,totalPayAndSeats[1]);
       }
       catch (InvalidPurchaseException e){
           throw e;
       }
       catch(Exception e){
           throw new InvalidPurchaseException(e.getMessage());
       }
    }

    /**
     * This private method is used to calculate the total payment due and the number of seats to be reserved
     */
    private int[] calculateTotalPaymentAndSeats(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException{

        int adultCount=0,infantCount = 0, childCount = 0;
        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            TicketTypeRequest.Type ticketType = ticketTypeRequest.getTicketType();
            int noOfTickets =ticketTypeRequest.getNoOfTickets();
            if(ticketType!=null && noOfTickets>0 ) {
                switch (ticketType) {
                    case INFANT:
                        infantCount += noOfTickets;
                        break;
                    case ADULT:
                        adultCount += noOfTickets;
                        break;
                    case CHILD:
                        childCount += noOfTickets;
                        break;
                    default:
                        break;
                }
            }
        }
        int totalTicketCount = adultCount+infantCount+childCount;
        if(totalTicketCount>MAX_PROCESSED_TICKET_LIMIT){
            throw new InvalidPurchaseException(Constants.EXCEEDS_MAX_TICKET_LIMIT);
        }
        else if (totalTicketCount==0){
            throw new InvalidPurchaseException(Constants.ZERO_VALID_TICKETS);
        }
        if (adultCount == 0) {
            throw new InvalidPurchaseException(Constants.ZERO_ADULT_TICKETS);
        }
        /*
          Added this condition assuming one adult can keep only one infant in his/her lap.
         */
        if (infantCount > adultCount) {
            throw new InvalidPurchaseException(Constants.MORE_INFANTS_THAN_ADULTS);
        }

        return new int[]{(adultCount * ADULT_TICKET_PRICE) + (childCount * CHILD_TICKET_PRICE) , childCount + adultCount};
    }
}
