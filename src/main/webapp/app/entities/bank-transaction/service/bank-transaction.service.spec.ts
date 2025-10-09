import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBankTransaction } from '../bank-transaction.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bank-transaction.test-samples';

import { BankTransactionService, RestBankTransaction } from './bank-transaction.service';

const requireRestSample: RestBankTransaction = {
  ...sampleWithRequiredData,
  transactionDate: sampleWithRequiredData.transactionDate?.toJSON(),
};

describe('BankTransaction Service', () => {
  let service: BankTransactionService;
  let httpMock: HttpTestingController;
  let expectedResult: IBankTransaction | IBankTransaction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BankTransactionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BankTransaction', () => {
      const bankTransaction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bankTransaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BankTransaction', () => {
      const bankTransaction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bankTransaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BankTransaction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BankTransaction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BankTransaction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBankTransactionToCollectionIfMissing', () => {
      it('should add a BankTransaction to an empty array', () => {
        const bankTransaction: IBankTransaction = sampleWithRequiredData;
        expectedResult = service.addBankTransactionToCollectionIfMissing([], bankTransaction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankTransaction);
      });

      it('should not add a BankTransaction to an array that contains it', () => {
        const bankTransaction: IBankTransaction = sampleWithRequiredData;
        const bankTransactionCollection: IBankTransaction[] = [
          {
            ...bankTransaction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBankTransactionToCollectionIfMissing(bankTransactionCollection, bankTransaction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BankTransaction to an array that doesn't contain it", () => {
        const bankTransaction: IBankTransaction = sampleWithRequiredData;
        const bankTransactionCollection: IBankTransaction[] = [sampleWithPartialData];
        expectedResult = service.addBankTransactionToCollectionIfMissing(bankTransactionCollection, bankTransaction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankTransaction);
      });

      it('should add only unique BankTransaction to an array', () => {
        const bankTransactionArray: IBankTransaction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bankTransactionCollection: IBankTransaction[] = [sampleWithRequiredData];
        expectedResult = service.addBankTransactionToCollectionIfMissing(bankTransactionCollection, ...bankTransactionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bankTransaction: IBankTransaction = sampleWithRequiredData;
        const bankTransaction2: IBankTransaction = sampleWithPartialData;
        expectedResult = service.addBankTransactionToCollectionIfMissing([], bankTransaction, bankTransaction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankTransaction);
        expect(expectedResult).toContain(bankTransaction2);
      });

      it('should accept null and undefined values', () => {
        const bankTransaction: IBankTransaction = sampleWithRequiredData;
        expectedResult = service.addBankTransactionToCollectionIfMissing([], null, bankTransaction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankTransaction);
      });

      it('should return initial array if no BankTransaction is added', () => {
        const bankTransactionCollection: IBankTransaction[] = [sampleWithRequiredData];
        expectedResult = service.addBankTransactionToCollectionIfMissing(bankTransactionCollection, undefined, null);
        expect(expectedResult).toEqual(bankTransactionCollection);
      });
    });

    describe('compareBankTransaction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBankTransaction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28516 };
        const entity2 = null;

        const compareResult1 = service.compareBankTransaction(entity1, entity2);
        const compareResult2 = service.compareBankTransaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28516 };
        const entity2 = { id: 28890 };

        const compareResult1 = service.compareBankTransaction(entity1, entity2);
        const compareResult2 = service.compareBankTransaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28516 };
        const entity2 = { id: 28516 };

        const compareResult1 = service.compareBankTransaction(entity1, entity2);
        const compareResult2 = service.compareBankTransaction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
