import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFuelLog } from '../fuel-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fuel-log.test-samples';

import { FuelLogService, RestFuelLog } from './fuel-log.service';

const requireRestSample: RestFuelLog = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('FuelLog Service', () => {
  let service: FuelLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IFuelLog | IFuelLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FuelLogService);
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

    it('should create a FuelLog', () => {
      const fuelLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fuelLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FuelLog', () => {
      const fuelLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fuelLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FuelLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FuelLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FuelLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFuelLogToCollectionIfMissing', () => {
      it('should add a FuelLog to an empty array', () => {
        const fuelLog: IFuelLog = sampleWithRequiredData;
        expectedResult = service.addFuelLogToCollectionIfMissing([], fuelLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fuelLog);
      });

      it('should not add a FuelLog to an array that contains it', () => {
        const fuelLog: IFuelLog = sampleWithRequiredData;
        const fuelLogCollection: IFuelLog[] = [
          {
            ...fuelLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFuelLogToCollectionIfMissing(fuelLogCollection, fuelLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FuelLog to an array that doesn't contain it", () => {
        const fuelLog: IFuelLog = sampleWithRequiredData;
        const fuelLogCollection: IFuelLog[] = [sampleWithPartialData];
        expectedResult = service.addFuelLogToCollectionIfMissing(fuelLogCollection, fuelLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fuelLog);
      });

      it('should add only unique FuelLog to an array', () => {
        const fuelLogArray: IFuelLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fuelLogCollection: IFuelLog[] = [sampleWithRequiredData];
        expectedResult = service.addFuelLogToCollectionIfMissing(fuelLogCollection, ...fuelLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fuelLog: IFuelLog = sampleWithRequiredData;
        const fuelLog2: IFuelLog = sampleWithPartialData;
        expectedResult = service.addFuelLogToCollectionIfMissing([], fuelLog, fuelLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fuelLog);
        expect(expectedResult).toContain(fuelLog2);
      });

      it('should accept null and undefined values', () => {
        const fuelLog: IFuelLog = sampleWithRequiredData;
        expectedResult = service.addFuelLogToCollectionIfMissing([], null, fuelLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fuelLog);
      });

      it('should return initial array if no FuelLog is added', () => {
        const fuelLogCollection: IFuelLog[] = [sampleWithRequiredData];
        expectedResult = service.addFuelLogToCollectionIfMissing(fuelLogCollection, undefined, null);
        expect(expectedResult).toEqual(fuelLogCollection);
      });
    });

    describe('compareFuelLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFuelLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32387 };
        const entity2 = null;

        const compareResult1 = service.compareFuelLog(entity1, entity2);
        const compareResult2 = service.compareFuelLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32387 };
        const entity2 = { id: 10395 };

        const compareResult1 = service.compareFuelLog(entity1, entity2);
        const compareResult2 = service.compareFuelLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32387 };
        const entity2 = { id: 32387 };

        const compareResult1 = service.compareFuelLog(entity1, entity2);
        const compareResult2 = service.compareFuelLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
