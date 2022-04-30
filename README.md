# ordo

Order validation system POC.

## Requirements

- An order has the following attributes, and these are stored as individual records in a file 
  - Order Id 
  - Order Qty 
  - Order Price 
  - Side – can be two values “Buy or Sell” 
  - Order Type – can be two values “LIMIT” or “MARKET”
- Each order is passed through a validator
  - NotionalValidator:
    - If OrderType is “LIMIT”, then validate if Qty*Price is < Configured Limit 
    - If OrderType is “MARKET”, then validate if Qty*DEFAULT_PRICE (100) < Configured Limit 
  - QtyValidator:  Validate if the Order Qty < 100,000 
  - TypeValidator: Validate if the Order is either “LIMIT” or “MARKET”
 

## Design principles

- test pyramid
- tdd

## Getting started

## Development

Lombok config for Idea:
- lombok plugin (recent ideas have it bundled)
- file > settings > Build, Execution, Deployment > Compiler > Annotation Processors > enable annotatin processing

## Technical choices

- maven wrapper

## Possible Improvements

