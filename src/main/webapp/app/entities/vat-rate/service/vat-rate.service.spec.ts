import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVATRate } from '../vat-rate.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vat-rate.test-samples';

import { VATRateService } from './vat-rate.service';

const requireRestSample: IVATRate = {
  ...sampleWithRequiredData,
};

describe('VATRate Service', () => {
  let service: VATRateService;
  let httpMock: HttpTestingController;
  let expectedResult: IVATRate | IVATRate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VATRateService);
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

    it('should create a VATRate', () => {
      const vATRate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vATRate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VATRate', () => {
      const vATRate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vATRate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VATRate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VATRate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VATRate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVATRateToCollectionIfMissing', () => {
      it('should add a VATRate to an empty array', () => {
        const vATRate: IVATRate = sampleWithRequiredData;
        expectedResult = service.addVATRateToCollectionIfMissing([], vATRate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vATRate);
      });

      it('should not add a VATRate to an array that contains it', () => {
        const vATRate: IVATRate = sampleWithRequiredData;
        const vATRateCollection: IVATRate[] = [
          {
            ...vATRate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVATRateToCollectionIfMissing(vATRateCollection, vATRate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VATRate to an array that doesn't contain it", () => {
        const vATRate: IVATRate = sampleWithRequiredData;
        const vATRateCollection: IVATRate[] = [sampleWithPartialData];
        expectedResult = service.addVATRateToCollectionIfMissing(vATRateCollection, vATRate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vATRate);
      });

      it('should add only unique VATRate to an array', () => {
        const vATRateArray: IVATRate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vATRateCollection: IVATRate[] = [sampleWithRequiredData];
        expectedResult = service.addVATRateToCollectionIfMissing(vATRateCollection, ...vATRateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vATRate: IVATRate = sampleWithRequiredData;
        const vATRate2: IVATRate = sampleWithPartialData;
        expectedResult = service.addVATRateToCollectionIfMissing([], vATRate, vATRate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vATRate);
        expect(expectedResult).toContain(vATRate2);
      });

      it('should accept null and undefined values', () => {
        const vATRate: IVATRate = sampleWithRequiredData;
        expectedResult = service.addVATRateToCollectionIfMissing([], null, vATRate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vATRate);
      });

      it('should return initial array if no VATRate is added', () => {
        const vATRateCollection: IVATRate[] = [sampleWithRequiredData];
        expectedResult = service.addVATRateToCollectionIfMissing(vATRateCollection, undefined, null);
        expect(expectedResult).toEqual(vATRateCollection);
      });
    });

    describe('compareVATRate', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVATRate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25659 };
        const entity2 = null;

        const compareResult1 = service.compareVATRate(entity1, entity2);
        const compareResult2 = service.compareVATRate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25659 };
        const entity2 = { id: 11155 };

        const compareResult1 = service.compareVATRate(entity1, entity2);
        const compareResult2 = service.compareVATRate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25659 };
        const entity2 = { id: 25659 };

        const compareResult1 = service.compareVATRate(entity1, entity2);
        const compareResult2 = service.compareVATRate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
