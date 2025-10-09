import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInvoiceLine } from '../invoice-line.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../invoice-line.test-samples';

import { InvoiceLineService } from './invoice-line.service';

const requireRestSample: IInvoiceLine = {
  ...sampleWithRequiredData,
};

describe('InvoiceLine Service', () => {
  let service: InvoiceLineService;
  let httpMock: HttpTestingController;
  let expectedResult: IInvoiceLine | IInvoiceLine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceLineService);
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

    it('should create a InvoiceLine', () => {
      const invoiceLine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(invoiceLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceLine', () => {
      const invoiceLine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(invoiceLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceLine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceLine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InvoiceLine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInvoiceLineToCollectionIfMissing', () => {
      it('should add a InvoiceLine to an empty array', () => {
        const invoiceLine: IInvoiceLine = sampleWithRequiredData;
        expectedResult = service.addInvoiceLineToCollectionIfMissing([], invoiceLine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceLine);
      });

      it('should not add a InvoiceLine to an array that contains it', () => {
        const invoiceLine: IInvoiceLine = sampleWithRequiredData;
        const invoiceLineCollection: IInvoiceLine[] = [
          {
            ...invoiceLine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInvoiceLineToCollectionIfMissing(invoiceLineCollection, invoiceLine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceLine to an array that doesn't contain it", () => {
        const invoiceLine: IInvoiceLine = sampleWithRequiredData;
        const invoiceLineCollection: IInvoiceLine[] = [sampleWithPartialData];
        expectedResult = service.addInvoiceLineToCollectionIfMissing(invoiceLineCollection, invoiceLine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceLine);
      });

      it('should add only unique InvoiceLine to an array', () => {
        const invoiceLineArray: IInvoiceLine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const invoiceLineCollection: IInvoiceLine[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceLineToCollectionIfMissing(invoiceLineCollection, ...invoiceLineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceLine: IInvoiceLine = sampleWithRequiredData;
        const invoiceLine2: IInvoiceLine = sampleWithPartialData;
        expectedResult = service.addInvoiceLineToCollectionIfMissing([], invoiceLine, invoiceLine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceLine);
        expect(expectedResult).toContain(invoiceLine2);
      });

      it('should accept null and undefined values', () => {
        const invoiceLine: IInvoiceLine = sampleWithRequiredData;
        expectedResult = service.addInvoiceLineToCollectionIfMissing([], null, invoiceLine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceLine);
      });

      it('should return initial array if no InvoiceLine is added', () => {
        const invoiceLineCollection: IInvoiceLine[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceLineToCollectionIfMissing(invoiceLineCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceLineCollection);
      });
    });

    describe('compareInvoiceLine', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInvoiceLine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8710 };
        const entity2 = null;

        const compareResult1 = service.compareInvoiceLine(entity1, entity2);
        const compareResult2 = service.compareInvoiceLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8710 };
        const entity2 = { id: 7582 };

        const compareResult1 = service.compareInvoiceLine(entity1, entity2);
        const compareResult2 = service.compareInvoiceLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8710 };
        const entity2 = { id: 8710 };

        const compareResult1 = service.compareInvoiceLine(entity1, entity2);
        const compareResult2 = service.compareInvoiceLine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
