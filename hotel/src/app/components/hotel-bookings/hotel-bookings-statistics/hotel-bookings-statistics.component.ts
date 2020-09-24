import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {RoomBookingDataModel} from "../../../models/roomBookingData.model";
import {RoomService} from "../../../services/room.service";

const START_DAY_BEFORE_TODAY = 5;
const END_DAY_AFTER_TODAY = 23;
const DAY_STEP = 24;

@Component({
  selector: 'hotel-bookings-statistics',
  templateUrl: './hotel-bookings-statistics.component.html',
  styleUrls: ['./hotel-bookings-statistics.component.css']
})
export class HotelBookingsStatisticsComponent implements OnInit {

  @Input() hotelId: BehaviorSubject<number>;
  roomBookingDataList: RoomBookingDataModel[];
  roomBookingDataListPrev: RoomBookingDataModel[];
  @ViewChild('canvas', {static: true})
  canvas: ElementRef<HTMLCanvasElement>;
  baseDate: Date;
  startDate: Date;
  endDate: Date;
  today: Date;
  chartType: string = 'bar';
  chartColors: Array<any> = [
    {
      cellBackground: 'hsl(10, 70%, 50%, .6)',
      backgroundColor: 'hsl(10, 70%, 50%, .6)',
      borderColor: 'hsl(10, 70%, 50%, .7)',
      borderWidth: 0,
    },
    {
      cellBackground: 'hsl(246, 70%, 50%, .6)',
      backgroundColor: 'hsl(246, 70%, 50%, .6)',
      borderColor: 'hsl(246, 70%, 50%, .7)',
      borderWidth: 0,
    }
  ];
  chartOptions: any = {
    responsive: true,
    scales: {
      yAxes: [{
        scaleLabel: {
          display: true,
          labelString: 'Napi Bevétel'
        }
      }]
    }
  };

  public chartDatasets: Array<any>;

  chartLabels: Array<string> = [];

  days: Array<number> = [];
  previousYearsDays: Array<number> = [];
  actualYearsIncomes: Array<number> = [];
  previousYearsIncomes: Array<number> = [];

  constructor(private roomService: RoomService) {
  }

  ngOnInit(): void {
    this.setToday();
    this.setBaseDateToday();
    this.hotelId.subscribe((id) => {
      if (id != 0) {
        this.doSomething(id);
      }
    });
  }
  setToday() {
    this.today = new Date();
    this.today.setHours(0, 0, 0, 0);
  }

  setBaseDateToday() {
    this.baseDate = new Date(this.today.getTime());
  }

  doSomething(hotelId: number) {
    this.setStartAndEndDate();
    this.createDayList();
    this.hotelId.subscribe((id) => {
      if (id != 0) {
        this.getRoomBookingDataList(id);
      }
    });
  }


  private getRoomBookingDataList(hotelId: number) {
    this.roomService.getRoomBookingData(hotelId, this.startDate, this.endDate).subscribe(
      (response: RoomBookingDataModel[]) => {
        this.roomBookingDataList = response;
        let previousStart = new Date(this.startDate.getTime());
        let previousEnd = new Date(this.endDate.getTime());
        previousStart.setFullYear(previousStart.getFullYear() - 1);
        previousEnd.setFullYear(previousEnd.getFullYear() - 1);
        this.roomService.getRoomBookingData(hotelId, previousStart, previousEnd).subscribe(
          (responsePrev) => {
            this.roomBookingDataListPrev = responsePrev;
            this.calculateIncomeInInterval();
          }
        );
      },
      error => console.warn(error)
    );
  }

  setStartAndEndDate() {
    this.startDate = new Date(this.baseDate.getTime());
    this.endDate = new Date(this.baseDate.getTime());
    this.startDate.setDate(this.startDate.getDate() - START_DAY_BEFORE_TODAY);
    this.endDate.setDate(this.endDate.getDate() + END_DAY_AFTER_TODAY);
  }

  private createDayList() {
    this.chartLabels = [];
    this.days = [];
    this.previousYearsDays = [];
    for (let actualDate = new Date(this.startDate.getTime()); actualDate.getTime() <= this.endDate.getTime(); actualDate.setDate(actualDate.getDate() + 1)) {
      this.days.push(actualDate.getTime());
      let previousYearDay = new Date(actualDate.getTime());
      previousYearDay.setFullYear(previousYearDay.getFullYear() - 1);
      this.previousYearsDays.push(previousYearDay.getTime());
      this.chartLabels.push(this.createDayDateString(actualDate));
    }
  }

  goBack() {
    this.baseDate.setDate(this.baseDate.getDate() - DAY_STEP);
    this.setStartAndEndDate();
    this.createDayList();
    this.hotelId.subscribe((id) => {
      if (id != 0) {
        this.getRoomBookingDataList(id);
      }
    });
  }

  goActual() {
    this.setBaseDateToday();
    this.setStartAndEndDate();
    this.createDayList();
    this.hotelId.subscribe((id) => {
      if (id != 0) {
        this.getRoomBookingDataList(id);
      }
    });
  }

  goNext() {
    this.baseDate.setDate(this.baseDate.getDate() + DAY_STEP);
    this.setStartAndEndDate();
    this.createDayList();
    this.hotelId.subscribe((id) => {
      if (id != 0) {
        this.getRoomBookingDataList(id);
      }
    });
  }

  private drawTodayLine(todayXOffset: number) {
    let context = this.canvas.nativeElement.getContext("2d");
    let height = context.canvas.height;
    let width = context.canvas.width;
    context.beginPath();
    context.moveTo(width * todayXOffset, 0);
    context.lineTo(width * todayXOffset, height);
    context.stroke();
  }


  calculateIncomeInInterval() {
    this.actualYearsIncomes = [];
    this.previousYearsIncomes = [];
    this.days.forEach((day) => {
      this.actualYearsIncomes.push(this.calculateDailyIncome(new Date(day), this.roomBookingDataList));
    });
    this.previousYearsDays.forEach((day) => {
      this.previousYearsIncomes.push(this.calculateDailyIncome(new Date(day), this.roomBookingDataListPrev));
    });
    this.chartDatasets = [
      {data: this.actualYearsIncomes, label: '2020'},
      {data: this.previousYearsIncomes, label: '2019'}
    ];

  }

  calculateDailyIncome(day: Date, roomBookings: RoomBookingDataModel[]): number {
    let dailyIncome: number = 0;
    roomBookings.forEach(
      (roomBookingDataModel) => {
        roomBookingDataModel.roomReservationDataList.forEach(
          (roomReservationDataModel) => {
            let roomReservationStartDate = new Date(roomReservationDataModel.startDate);
            let roomReservationEndDate = new Date(roomReservationDataModel.endDate);
            roomReservationStartDate.setHours(0, 0, 0, 0);
            roomReservationEndDate.setHours(0, 0, 0, 0);
            if (day.getTime() >= roomReservationStartDate.getTime() && day.getTime() < roomReservationEndDate.getTime()) {
              dailyIncome += roomBookingDataModel.pricePerNight;
            }
          }
        )
      }
    );
    return dailyIncome;
  }

  private createDayDateString(date: Date) {
    let result: string = '';
    let month = date.getMonth() + 1;
    if (month.toString().length === 1) {
      result += '0';
    }
    result += month + '.';
    if (date.getDate().toString().length === 1) {
      result += '0';
    }
    result += date.getDate() + '.';
    return result;
  }


}
