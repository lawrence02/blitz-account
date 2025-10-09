import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFleetTrip } from '../fleet-trip.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fleet-trip.test-samples';

import { FleetTripService, RestFleetTrip } from './fleet-trip.service';

const requireRestSample: RestFleetTrip = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('FleetTrip Service', () => {
  let service: FleetTripService;
  let httpMock: HttpTestingController;
  let expectedResult: IFleetTrip | IFleetTrip[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FleetTripService);
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

    it('should create a FleetTrip', () => {
      const fleetTrip = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fleetTrip).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FleetTrip', () => {
      const fleetTrip = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fleetTrip).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FleetTrip', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FleetTrip', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FleetTrip', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFleetTripToCollectionIfMissing', () => {
      it('should add a FleetTrip to an empty array', () => {
        const fleetTrip: IFleetTrip = sampleWithRequiredData;
        expectedResult = service.addFleetTripToCollectionIfMissing([], fleetTrip);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fleetTrip);
      });

      it('should not add a FleetTrip to an array that contains it', () => {
        const fleetTrip: IFleetTrip = sampleWithRequiredData;
        const fleetTripCollection: IFleetTrip[] = [
          {
            ...fleetTrip,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFleetTripToCollectionIfMissing(fleetTripCollection, fleetTrip);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FleetTrip to an array that doesn't contain it", () => {
        const fleetTrip: IFleetTrip = sampleWithRequiredData;
        const fleetTripCollection: IFleetTrip[] = [sampleWithPartialData];
        expectedResult = service.addFleetTripToCollectionIfMissing(fleetTripCollection, fleetTrip);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fleetTrip);
      });

      it('should add only unique FleetTrip to an array', () => {
        const fleetTripArray: IFleetTrip[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fleetTripCollection: IFleetTrip[] = [sampleWithRequiredData];
        expectedResult = service.addFleetTripToCollectionIfMissing(fleetTripCollection, ...fleetTripArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fleetTrip: IFleetTrip = sampleWithRequiredData;
        const fleetTrip2: IFleetTrip = sampleWithPartialData;
        expectedResult = service.addFleetTripToCollectionIfMissing([], fleetTrip, fleetTrip2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fleetTrip);
        expect(expectedResult).toContain(fleetTrip2);
      });

      it('should accept null and undefined values', () => {
        const fleetTrip: IFleetTrip = sampleWithRequiredData;
        expectedResult = service.addFleetTripToCollectionIfMissing([], null, fleetTrip, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fleetTrip);
      });

      it('should return initial array if no FleetTrip is added', () => {
        const fleetTripCollection: IFleetTrip[] = [sampleWithRequiredData];
        expectedResult = service.addFleetTripToCollectionIfMissing(fleetTripCollection, undefined, null);
        expect(expectedResult).toEqual(fleetTripCollection);
      });
    });

    describe('compareFleetTrip', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFleetTrip(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2208 };
        const entity2 = null;

        const compareResult1 = service.compareFleetTrip(entity1, entity2);
        const compareResult2 = service.compareFleetTrip(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2208 };
        const entity2 = { id: 30736 };

        const compareResult1 = service.compareFleetTrip(entity1, entity2);
        const compareResult2 = service.compareFleetTrip(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2208 };
        const entity2 = { id: 2208 };

        const compareResult1 = service.compareFleetTrip(entity1, entity2);
        const compareResult2 = service.compareFleetTrip(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
