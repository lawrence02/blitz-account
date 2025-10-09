import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFleetTripLocation } from '../fleet-trip-location.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fleet-trip-location.test-samples';

import { FleetTripLocationService, RestFleetTripLocation } from './fleet-trip-location.service';

const requireRestSample: RestFleetTripLocation = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('FleetTripLocation Service', () => {
  let service: FleetTripLocationService;
  let httpMock: HttpTestingController;
  let expectedResult: IFleetTripLocation | IFleetTripLocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FleetTripLocationService);
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

    it('should create a FleetTripLocation', () => {
      const fleetTripLocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fleetTripLocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FleetTripLocation', () => {
      const fleetTripLocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fleetTripLocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FleetTripLocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FleetTripLocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FleetTripLocation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFleetTripLocationToCollectionIfMissing', () => {
      it('should add a FleetTripLocation to an empty array', () => {
        const fleetTripLocation: IFleetTripLocation = sampleWithRequiredData;
        expectedResult = service.addFleetTripLocationToCollectionIfMissing([], fleetTripLocation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fleetTripLocation);
      });

      it('should not add a FleetTripLocation to an array that contains it', () => {
        const fleetTripLocation: IFleetTripLocation = sampleWithRequiredData;
        const fleetTripLocationCollection: IFleetTripLocation[] = [
          {
            ...fleetTripLocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFleetTripLocationToCollectionIfMissing(fleetTripLocationCollection, fleetTripLocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FleetTripLocation to an array that doesn't contain it", () => {
        const fleetTripLocation: IFleetTripLocation = sampleWithRequiredData;
        const fleetTripLocationCollection: IFleetTripLocation[] = [sampleWithPartialData];
        expectedResult = service.addFleetTripLocationToCollectionIfMissing(fleetTripLocationCollection, fleetTripLocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fleetTripLocation);
      });

      it('should add only unique FleetTripLocation to an array', () => {
        const fleetTripLocationArray: IFleetTripLocation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fleetTripLocationCollection: IFleetTripLocation[] = [sampleWithRequiredData];
        expectedResult = service.addFleetTripLocationToCollectionIfMissing(fleetTripLocationCollection, ...fleetTripLocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fleetTripLocation: IFleetTripLocation = sampleWithRequiredData;
        const fleetTripLocation2: IFleetTripLocation = sampleWithPartialData;
        expectedResult = service.addFleetTripLocationToCollectionIfMissing([], fleetTripLocation, fleetTripLocation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fleetTripLocation);
        expect(expectedResult).toContain(fleetTripLocation2);
      });

      it('should accept null and undefined values', () => {
        const fleetTripLocation: IFleetTripLocation = sampleWithRequiredData;
        expectedResult = service.addFleetTripLocationToCollectionIfMissing([], null, fleetTripLocation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fleetTripLocation);
      });

      it('should return initial array if no FleetTripLocation is added', () => {
        const fleetTripLocationCollection: IFleetTripLocation[] = [sampleWithRequiredData];
        expectedResult = service.addFleetTripLocationToCollectionIfMissing(fleetTripLocationCollection, undefined, null);
        expect(expectedResult).toEqual(fleetTripLocationCollection);
      });
    });

    describe('compareFleetTripLocation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFleetTripLocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24349 };
        const entity2 = null;

        const compareResult1 = service.compareFleetTripLocation(entity1, entity2);
        const compareResult2 = service.compareFleetTripLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24349 };
        const entity2 = { id: 23545 };

        const compareResult1 = service.compareFleetTripLocation(entity1, entity2);
        const compareResult2 = service.compareFleetTripLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24349 };
        const entity2 = { id: 24349 };

        const compareResult1 = service.compareFleetTripLocation(entity1, entity2);
        const compareResult2 = service.compareFleetTripLocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
