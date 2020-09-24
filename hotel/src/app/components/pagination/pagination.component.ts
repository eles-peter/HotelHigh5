import {AfterViewChecked, Component, EventEmitter, Input, OnInit, Output} from "@angular/core";

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements AfterViewChecked{
  @Input()fullNumberOfPages: number;
  pageNumbers: number[] = [];
  listPageNumber: number = 0;
  @Output()emittedListPageNumber = new EventEmitter<number>();

  ngAfterViewChecked() {
    this.pageNumbers = this.generatePageNumberArray();
  }

  onPageNumClick(number: number) {
    this.listPageNumber = number;
    this.emittedListPageNumber.emit(this.listPageNumber);
    this.pageNumbers = this.generatePageNumberArray();
  }

  generatePageNumberArray() {
    //TODO write so that always 7 blocks are visible
    let numArray = new Array<number>();
    if (this.listPageNumber === this.fullNumberOfPages - 1 && this.listPageNumber > 2) {
      numArray.push(this.listPageNumber - 2);
    }
    if (this.listPageNumber > 1) {
      numArray.push(this.listPageNumber - 1);
    }
    if (this.listPageNumber !== 0 && this.listPageNumber !== this.fullNumberOfPages - 1) {
      numArray.push(this.listPageNumber);
    }
    if (this.listPageNumber < this.fullNumberOfPages - 2) {
      numArray.push(this.listPageNumber + 1);
    }
    if (this.listPageNumber === 0 && this.fullNumberOfPages > 3) {
      numArray.push(this.listPageNumber + 2);
    }
    return numArray;
  }

}
