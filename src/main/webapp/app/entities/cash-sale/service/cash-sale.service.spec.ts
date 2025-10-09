import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICashSale } from '../cash-sale.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cash-sale.test-samples';

import { CashSaleService, RestCashSale } from './cash-sale.service';

const requireRestSample: RestCashSale = {
  ...sampleWithRequiredData,
  saleDate: sampleWithRequiredData.saleDate?.toJSON(),
};

describe('CashSale Service', () => {
  let service: CashSaleService;
  let httpMock: HttpTestingController;
  let expectedResult: ICashSale | ICashSale[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CashSaleService);
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

    it('should create a CashSale', () => {
      const cashSale = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cashSale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CashSale', () => {
      const cashSale = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cashSale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CashSale', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CashSale', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CashSale', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCashSaleToCollectionIfMissing', () => {
      it('should add a CashSale to an empty array', () => {
        const cashSale: ICashSale = sampleWithRequiredData;
        expectedResult = service.addCashSaleToCollectionIfMissing([], cashSale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cashSale);
      });

      it('should not add a CashSale to an array that contains it', () => {
        const cashSale: ICashSale = sampleWithRequiredData;
        const cashSaleCollection: ICashSale[] = [
          {
            ...cashSale,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCashSaleToCollectionIfMissing(cashSaleCollection, cashSale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CashSale to an array that doesn't contain it", () => {
        const cashSale: ICashSale = sampleWithRequiredData;
        const cashSaleCollection: ICashSale[] = [sampleWithPartialData];
        expectedResult = service.addCashSaleToCollectionIfMissing(cashSaleCollection, cashSale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cashSale);
      });

      it('should add only unique CashSale to an array', () => {
        const cashSaleArray: ICashSale[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cashSaleCollection: ICashSale[] = [sampleWithRequiredData];
        expectedResult = service.addCashSaleToCollectionIfMissing(cashSaleCollection, ...cashSaleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cashSale: ICashSale = sampleWithRequiredData;
        const cashSale2: ICashSale = sampleWithPartialData;
        expectedResult = service.addCashSaleToCollectionIfMissing([], cashSale, cashSale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cashSale);
        expect(expectedResult).toContain(cashSale2);
      });

      it('should accept null and undefined values', () => {
        const cashSale: ICashSale = sampleWithRequiredData;
        expectedResult = service.addCashSaleToCollectionIfMissing([], null, cashSale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cashSale);
      });

      it('should return initial array if no CashSale is added', () => {
        const cashSaleCollection: ICashSale[] = [sampleWithRequiredData];
        expectedResult = service.addCashSaleToCollectionIfMissing(cashSaleCollection, undefined, null);
        expect(expectedResult).toEqual(cashSaleCollection);
      });
    });

    describe('compareCashSale', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCashSale(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18922 };
        const entity2 = null;

        const compareResult1 = service.compareCashSale(entity1, entity2);
        const compareResult2 = service.compareCashSale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18922 };
        const entity2 = { id: 23860 };

        const compareResult1 = service.compareCashSale(entity1, entity2);
        const compareResult2 = service.compareCashSale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18922 };
        const entity2 = { id: 18922 };

        const compareResult1 = service.compareCashSale(entity1, entity2);
        const compareResult2 = service.compareCashSale(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
