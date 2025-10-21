// ==========================================
// TEACHER HIGHLIGHT COMPONENT
// Save as: src/components/home/TeacherHighlight.tsx
// ==========================================

import { ChevronRight } from 'lucide-react';
import { TEACHER_DATA } from '../../config/homePageConfig';

export const TeacherHighlight = () => {
  return (
    <section className="py-16 md:py-24 bg-gradient-to-br from-primary/5 to-secondary/5">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            {TEACHER_DATA.tagline}
          </h2>
          <p className="text-muted-foreground">
            Expert educators with years of experience and proven results
          </p>
        </div>

        <div className="grid md:grid-cols-2 gap-12 items-center max-w-6xl mx-auto">
          {/* Teacher Image */}
          <div className="relative">
            <div className="relative rounded-2xl overflow-hidden shadow-2xl">
              <img
                src={TEACHER_DATA.teacherImage}
                alt={TEACHER_DATA.teacherName}
                className="w-full h-[500px] object-cover"
              />
              <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/80 to-transparent p-6">
                <h3 className="text-white text-2xl font-bold">{TEACHER_DATA.teacherName}</h3>
                <p className="text-white/90">{TEACHER_DATA.teacherRole}</p>
              </div>
            </div>

            {/* Stats Overlay */}
            <div className="absolute -bottom-8 left-1/2 -translate-x-1/2 bg-white rounded-lg shadow-xl p-4 flex gap-8 w-11/12">
              {TEACHER_DATA.stats.map((stat, index) => (
                <div key={index} className="text-center flex-1">
                  <div className="text-2xl font-bold text-primary">{stat.value}</div>
                  <div className="text-sm text-muted-foreground">{stat.label}</div>
                </div>
              ))}
            </div>
          </div>

          {/* Features List */}
          <div className="space-y-6 mt-12 md:mt-0">
            {TEACHER_DATA.features.map((feature, index) => {
              const Icon = feature.icon;
              return (
                <div key={index} className="flex gap-4 p-4 rounded-lg hover:bg-accent/50 transition-colors">
                  <div className="flex-shrink-0">
                    <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
                      <Icon className="h-6 w-6 text-primary" />
                    </div>
                  </div>
                  <div>
                    <h4 className="font-semibold mb-1">{feature.title}</h4>
                    <p className="text-sm text-muted-foreground">{feature.description}</p>
                  </div>
                </div>
              );
            })}

            <div className="pt-4">
              <a
                href="/courses"
                className="inline-flex items-center gap-2 bg-primary text-primary-foreground px-6 py-3 rounded-lg font-semibold hover:bg-primary/90 transition-colors"
              >
                Get Started
                <ChevronRight className="h-5 w-5" />
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};
