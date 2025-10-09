import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IChartOfAccount } from '../chart-of-account.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../chart-of-account.test-samples';

import { ChartOfAccountService } from './chart-of-account.service';

const requireRestSample: IChartOfAccount = {
  ...sampleWithRequiredData,
};

describe('ChartOfAccount Service', () => {
  let service: ChartOfAccountService;
  let httpMock: HttpTestingController;
  let expectedResult: IChartOfAccount | IChartOfAccount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ChartOfAccountService);
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

    it('should create a ChartOfAccount', () => {
      const chartOfAccount = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(chartOfAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChartOfAccount', () => {
      const chartOfAccount = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(chartOfAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChartOfAccount', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChartOfAccount', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ChartOfAccount', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addChartOfAccountToCollectionIfMissing', () => {
      it('should add a ChartOfAccount to an empty array', () => {
        const chartOfAccount: IChartOfAccount = sampleWithRequiredData;
        expectedResult = service.addChartOfAccountToCollectionIfMissing([], chartOfAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chartOfAccount);
      });

      it('should not add a ChartOfAccount to an array that contains it', () => {
        const chartOfAccount: IChartOfAccount = sampleWithRequiredData;
        const chartOfAccountCollection: IChartOfAccount[] = [
          {
            ...chartOfAccount,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChartOfAccountToCollectionIfMissing(chartOfAccountCollection, chartOfAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChartOfAccount to an array that doesn't contain it", () => {
        const chartOfAccount: IChartOfAccount = sampleWithRequiredData;
        const chartOfAccountCollection: IChartOfAccount[] = [sampleWithPartialData];
        expectedResult = service.addChartOfAccountToCollectionIfMissing(chartOfAccountCollection, chartOfAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chartOfAccount);
      });

      it('should add only unique ChartOfAccount to an array', () => {
        const chartOfAccountArray: IChartOfAccount[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const chartOfAccountCollection: IChartOfAccount[] = [sampleWithRequiredData];
        expectedResult = service.addChartOfAccountToCollectionIfMissing(chartOfAccountCollection, ...chartOfAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const chartOfAccount: IChartOfAccount = sampleWithRequiredData;
        const chartOfAccount2: IChartOfAccount = sampleWithPartialData;
        expectedResult = service.addChartOfAccountToCollectionIfMissing([], chartOfAccount, chartOfAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chartOfAccount);
        expect(expectedResult).toContain(chartOfAccount2);
      });

      it('should accept null and undefined values', () => {
        const chartOfAccount: IChartOfAccount = sampleWithRequiredData;
        expectedResult = service.addChartOfAccountToCollectionIfMissing([], null, chartOfAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chartOfAccount);
      });

      it('should return initial array if no ChartOfAccount is added', () => {
        const chartOfAccountCollection: IChartOfAccount[] = [sampleWithRequiredData];
        expectedResult = service.addChartOfAccountToCollectionIfMissing(chartOfAccountCollection, undefined, null);
        expect(expectedResult).toEqual(chartOfAccountCollection);
      });
    });

    describe('compareChartOfAccount', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareChartOfAccount(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20785 };
        const entity2 = null;

        const compareResult1 = service.compareChartOfAccount(entity1, entity2);
        const compareResult2 = service.compareChartOfAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20785 };
        const entity2 = { id: 20252 };

        const compareResult1 = service.compareChartOfAccount(entity1, entity2);
        const compareResult2 = service.compareChartOfAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20785 };
        const entity2 = { id: 20785 };

        const compareResult1 = service.compareChartOfAccount(entity1, entity2);
        const compareResult2 = service.compareChartOfAccount(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
