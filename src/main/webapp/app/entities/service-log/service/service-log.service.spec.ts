import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IServiceLog } from '../service-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../service-log.test-samples';

import { RestServiceLog, ServiceLogService } from './service-log.service';

const requireRestSample: RestServiceLog = {
  ...sampleWithRequiredData,
  serviceDate: sampleWithRequiredData.serviceDate?.toJSON(),
};

describe('ServiceLog Service', () => {
  let service: ServiceLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceLog | IServiceLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceLogService);
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

    it('should create a ServiceLog', () => {
      const serviceLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceLog', () => {
      const serviceLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServiceLogToCollectionIfMissing', () => {
      it('should add a ServiceLog to an empty array', () => {
        const serviceLog: IServiceLog = sampleWithRequiredData;
        expectedResult = service.addServiceLogToCollectionIfMissing([], serviceLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceLog);
      });

      it('should not add a ServiceLog to an array that contains it', () => {
        const serviceLog: IServiceLog = sampleWithRequiredData;
        const serviceLogCollection: IServiceLog[] = [
          {
            ...serviceLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceLogToCollectionIfMissing(serviceLogCollection, serviceLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceLog to an array that doesn't contain it", () => {
        const serviceLog: IServiceLog = sampleWithRequiredData;
        const serviceLogCollection: IServiceLog[] = [sampleWithPartialData];
        expectedResult = service.addServiceLogToCollectionIfMissing(serviceLogCollection, serviceLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceLog);
      });

      it('should add only unique ServiceLog to an array', () => {
        const serviceLogArray: IServiceLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceLogCollection: IServiceLog[] = [sampleWithRequiredData];
        expectedResult = service.addServiceLogToCollectionIfMissing(serviceLogCollection, ...serviceLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceLog: IServiceLog = sampleWithRequiredData;
        const serviceLog2: IServiceLog = sampleWithPartialData;
        expectedResult = service.addServiceLogToCollectionIfMissing([], serviceLog, serviceLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceLog);
        expect(expectedResult).toContain(serviceLog2);
      });

      it('should accept null and undefined values', () => {
        const serviceLog: IServiceLog = sampleWithRequiredData;
        expectedResult = service.addServiceLogToCollectionIfMissing([], null, serviceLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceLog);
      });

      it('should return initial array if no ServiceLog is added', () => {
        const serviceLogCollection: IServiceLog[] = [sampleWithRequiredData];
        expectedResult = service.addServiceLogToCollectionIfMissing(serviceLogCollection, undefined, null);
        expect(expectedResult).toEqual(serviceLogCollection);
      });
    });

    describe('compareServiceLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2909 };
        const entity2 = null;

        const compareResult1 = service.compareServiceLog(entity1, entity2);
        const compareResult2 = service.compareServiceLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2909 };
        const entity2 = { id: 23397 };

        const compareResult1 = service.compareServiceLog(entity1, entity2);
        const compareResult2 = service.compareServiceLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2909 };
        const entity2 = { id: 2909 };

        const compareResult1 = service.compareServiceLog(entity1, entity2);
        const compareResult2 = service.compareServiceLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
