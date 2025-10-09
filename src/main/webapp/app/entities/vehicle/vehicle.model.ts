import { VehicleStatus } from 'app/entities/enumerations/vehicle-status.model';

export interface IVehicle {
  id: number;
  name?: string | null;
  licensePlate?: string | null;
  type?: string | null;
  currentMileage?: number | null;
  status?: keyof typeof VehicleStatus | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };
