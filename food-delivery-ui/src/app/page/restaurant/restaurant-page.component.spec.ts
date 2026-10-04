import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RestaurantPageComponent } from './restaurant-page.component';

describe('RestaurantPageComponent', () => {
  let component: RestaurantPageComponent;
  let fixture: ComponentFixture<RestaurantPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RestaurantPageComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RestaurantPageComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
