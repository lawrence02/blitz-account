import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IIncidentLog } from '../incident-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../incident-log.test-samples';

import { IncidentLogService, RestIncidentLog } from './incident-log.service';

const requireRestSample: RestIncidentLog = {
  ...sampleWithRequiredData,
  incidentDate: sampleWithRequiredData.incidentDate?.toJSON(),
};

describe('IncidentLog Service', () => {
  let service: IncidentLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IIncidentLog | IIncidentLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IncidentLogService);
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

    it('should create a IncidentLog', () => {
      const incidentLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(incidentLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncidentLog', () => {
      const incidentLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(incidentLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IncidentLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IncidentLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IncidentLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIncidentLogToCollectionIfMissing', () => {
      it('should add a IncidentLog to an empty array', () => {
        const incidentLog: IIncidentLog = sampleWithRequiredData;
        expectedResult = service.addIncidentLogToCollectionIfMissing([], incidentLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incidentLog);
      });

      it('should not add a IncidentLog to an array that contains it', () => {
        const incidentLog: IIncidentLog = sampleWithRequiredData;
        const incidentLogCollection: IIncidentLog[] = [
          {
            ...incidentLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIncidentLogToCollectionIfMissing(incidentLogCollection, incidentLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncidentLog to an array that doesn't contain it", () => {
        const incidentLog: IIncidentLog = sampleWithRequiredData;
        const incidentLogCollection: IIncidentLog[] = [sampleWithPartialData];
        expectedResult = service.addIncidentLogToCollectionIfMissing(incidentLogCollection, incidentLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incidentLog);
      });

      it('should add only unique IncidentLog to an array', () => {
        const incidentLogArray: IIncidentLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const incidentLogCollection: IIncidentLog[] = [sampleWithRequiredData];
        expectedResult = service.addIncidentLogToCollectionIfMissing(incidentLogCollection, ...incidentLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incidentLog: IIncidentLog = sampleWithRequiredData;
        const incidentLog2: IIncidentLog = sampleWithPartialData;
        expectedResult = service.addIncidentLogToCollectionIfMissing([], incidentLog, incidentLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incidentLog);
        expect(expectedResult).toContain(incidentLog2);
      });

      it('should accept null and undefined values', () => {
        const incidentLog: IIncidentLog = sampleWithRequiredData;
        expectedResult = service.addIncidentLogToCollectionIfMissing([], null, incidentLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incidentLog);
      });

      it('should return initial array if no IncidentLog is added', () => {
        const incidentLogCollection: IIncidentLog[] = [sampleWithRequiredData];
        expectedResult = service.addIncidentLogToCollectionIfMissing(incidentLogCollection, undefined, null);
        expect(expectedResult).toEqual(incidentLogCollection);
      });
    });

    describe('compareIncidentLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIncidentLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5606 };
        const entity2 = null;

        const compareResult1 = service.compareIncidentLog(entity1, entity2);
        const compareResult2 = service.compareIncidentLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5606 };
        const entity2 = { id: 593 };

        const compareResult1 = service.compareIncidentLog(entity1, entity2);
        const compareResult2 = service.compareIncidentLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5606 };
        const entity2 = { id: 5606 };

        const compareResult1 = service.compareIncidentLog(entity1, entity2);
        const compareResult2 = service.compareIncidentLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
