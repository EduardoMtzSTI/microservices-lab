interface CustomerData {
  fullName: string;
  email: string;
  number: string;
  address: string;
}

export interface Customer extends CustomerData {
  id: string;
}

export interface NewCustomer extends CustomerData {}
