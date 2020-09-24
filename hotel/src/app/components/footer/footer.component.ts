import {Component} from '@angular/core';
import {scrollToTheTop} from "../../utils/smoothScroller";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
})
export class FooterComponent {

  gotoTop() {
    scrollToTheTop(100);
  }

}
